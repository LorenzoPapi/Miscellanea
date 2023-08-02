import os, sys, gc, potrace, cv2, numpy, potrace, time
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
from matplotlib.path import Path
from matplotlib.patches import PathPatch, Polygon
from youtube_dl import YoutubeDL as ytdl
from subprocess import Popen, PIPE

nudge = 0.33
lims = []

## NOT GONNA DO MULTITHREAD FOR NOW.
## EVERYTHING IS GONNA BE SINGLE-THREADED.

fig, ax = plt.subplots()
total = 0
skipped = 0 #skipped frames

def trace_and_render(frame, edges):
    global total
    start = time.time()
    hist = cv2.calcHist([edges], [0], None, [2], [0,256])
    for i in range(len(edges)):
        edges[i][edges[i] > 1] = 1
    bmp = potrace.Bitmap(edges[::-1])
    path = bmp.trace(10 if hist[1] == 0 else hist[0]/hist[1], potrace.TURNPOLICY_MINORITY, 4/3, 1, 0.5)
    print(f"Rendering frame {frame}")
    fig.suptitle(f"Frame: {frame}", fontsize = 16)
    plt.xlim([0, lims[0]])
    plt.ylim([0, lims[1]])
    print("Curves: " + str(len(path.curves)))
    for curve in path.curves:
        render_curve(curve)
    fig.savefig(f"figures/frame-{frame}.png", dpi=200)
    ax.cla()
    gc.collect()
    delta = time.time() - start
    total += delta
    print(f"Made frame in {delta} seconds.")
    
def render_curve(curve):
    start = curve.start_point
    for segment in curve.segments:
        if segment.is_corner:
            p = Polygon([start, segment.c, segment.end_point], True, fc="none")
        else:
            p = PathPatch(Path([start, segment.c1, segment.c2, segment.end_point], [Path.MOVETO, Path.CURVE4, Path.CURVE4, Path.CURVE4]), fc="none")
        ax.add_patch(p)
        start = segment.end_point
    #plt.draw()
    #plt.pause(0.001)

def make_contours(frame):
    edge_out = f"edges/frame-{frame}.png"
    if not os.path.exists(edge_out):
        img = cv2.imread(f'frames/frame-{frame}.png')
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        median = max(10, min(245, numpy.median(gray)))
        lower = int(max(0, (1 - nudge) * median))
        upper = int(min(255, (1 + nudge) * median))
        filtered = cv2.bilateralFilter(gray, 5, 50, 50)
        edges = cv2.Canny(filtered, lower, upper, apertureSize=5)
        cv2.imwrite(edge_out, edges)
    else:
        img = cv2.imread(edge_out)
        edges = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    #trace_and_render(frame, edges)

def main():
    global lims, skipped
    try:
        os.mkdir("current")
    except:
        pass
    os.chdir(os.getcwd() + "/current")
    try:
        os.mkdir("frames")
        os.mkdir("edges")
        os.mkdir("figures")
    except:
        pass
    
    args = sys.argv

    print("Graphic calculator started. If you don't have youtube-dl or yt-dlp installed in your system, you need to put the video you want to convert inside the folder of the graph.py file and name it as 'video.mp4'")
    
    if os.path.exists("video.mp4"):
        print("Video already exists.")
    else:
        if len(args) == 1:
            print(f"No arguments given. Use as \'python3 {args[0]} URL\'")
            sys.exit(0)
        else:
            Popen(f"youtube-dl -f bestvideo[ext=mp4]+bestaudio[ext=m4a] -o video.mp4 {args[1]}".split()).communicate()
    
    if not os.path.exists("audio.m4a"):
        print(execute_ff("-i video.mp4 -vn audio.m4a"))
    
    frames = len(os.listdir("frames"))
    if frames == 0:
        print("Generating frames...")
        execute_ff(f"-i video.mp4 -vsync 0 -format image2 frames/frame-%d.png")
    frames = len(os.listdir("frames"))
    print(f"Generated {frames} frames...")

    lims = [int(i) for i in execute_ff("stream=width,height -of csv=p=0 video.mp4", False).strip().split(",")]

    for f in range(1, frames+1):
        if not os.path.exists(f"figures/frame-{f}.png"):
            make_contours(str(f))
        else:
            skipped += 1

    avg = total/(frames - skipped)
    print(f"Took in average {avg} seconds per frame.")
    print(f"Took in total {total} seconds to do {frames - skipped} frames.")

    frame_rate = execute_ff("stream=r_frame_rate -of csv=p=0 video.mp4", False)
    execute_ff(f"-r {frame_rate} -start_number 1 -i figures/frame-%d.png -i audio.m4a -vframes {frames} -c:v libx264 -profile:v baseline -crf 25 -level 3.0 -pix_fmt yuv420p output.mp4")

def execute_ff(cmnd, mpeg=True):
    p = Popen(("ffmpeg -y".split() if mpeg else "ffprobe -v error -select_streams v:0 -show_entries".split()) + cmnd.split(), stdout=PIPE, stderr=PIPE).communicate()
    return p[0].decode().strip()

if __name__ == "__main__":
    main()
