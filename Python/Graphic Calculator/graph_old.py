import cv2 as cv
import sys
import os
import numpy as np
import potrace
import matplotlib.pyplot as plt
from matplotlib.path import Path 
from matplotlib.patches import PathPatch, Polygon
import youtube_dl as ydl
from ffmpy import FFmpeg as ff
from threading import *
import gc
from time import sleep
from math import ceil

FOLDER = "frames"
minh = 100
threads = []
figures = []

def render(xlim, ylim, frame, path, fig):
    print("Mapped frame " + str(frame) + ", now rendering")
    fig.suptitle("Frame: " + str(frame), fontsize = 16)
    a1 = fig.gca()
    a1.cla()
    a1.set_xlim(0, xlim)
    a1.set_ylim(0, ylim)
    a1.grid()
    for curve in path.curves:
        segments = curve.segments
        start = curve.start_point
        for segment in segments:
            if segment.is_corner:
                p = Polygon([start, segment.c, segment.end_point], False, fill=False)
            else:
                p = PathPatch(Path([start, segment.c1, segment.c2, segment.end_point], [1, 4, 4, 4]), fill=False)
            a1.add_patch(p)
            start = segment.end_point
    fig.savefig("figures/fig-" + str(frame) + ".png")
    print("Finished rendering frame " +  str(frame))

def getContoursFromEdges(edges, frame):
    cont, _ = cv.findContours(edges, cv.RETR_TREE, cv.CHAIN_APPROX_SIMPLE)
    ylim, xlim = edges.shape
    drawing = np.zeros((ylim, xlim), dtype=np.uint8)
    cv.drawContours(drawing, cont, -1, (255, 255, 255), 10)
    drawing = cv.erode(drawing, np.ones((5, 5), np.uint8), iterations = 2)
    cv.imwrite("contours/frame-" + str(frame) + ".png", drawing)
    return drawing

def makeContours(frame, fig):
    if not (os.path.exists("contours/frame-" + str(frame) + ".png")):
        img = cv.imread(FOLDER + "/frame-" + str(frame) + ".png", cv.IMREAD_GRAYSCALE)
        img = cv.equalizeHist(img)
        edges = cv.Canny(img, minh, minh * 2)
        ylim, xlim = edges.shape
        cv.imwrite("edges/frame-" + str(frame) + ".png", edges)
        totrace = getContoursFromEdges(edges, frame)
        print("Made contours for frame " + str(frame))
    else:
        totrace = cv.imread("contours/frame-" + str(frame) + ".png", cv.IMREAD_GRAYSCALE)
        ylim, xlim = totrace.shape
        print("Read contours for frame " + str(frame))
    for i in range(len(totrace)):
        totrace[i][totrace[i] > 0] = 1
    path = potrace.Bitmap(totrace[::-1]).trace()
    render(xlim, ylim, frame, path, fig)
    gc.collect()

class RenderThread(Thread):
    def __init__(self, frames, fig):
        Thread.__init__(self)
        self.frames = frames
        self.fig = fig
        self.finished = False
    def run(self):
        for f in self.frames:
            if (not os.path.exists("figures/fig-" + str(f) + ".png")):
                makeContours(f, self.fig)
            else:
                print("Render for frame " + str(f) + " already done, skipping...")
        self.finished = True

def main():
    args = sys.argv
    if os.path.exists("video.mp4"):
        if (len(args) > 1):
            print("Warning: a video has already been downloaded. Ignoring...")
    else:
        if (len(args) == 1):
            print("No arguments given. Use as \'python3 load.py (url)\'")
            sys.exit(0)
        else:
            url = args[1]
            ydl.YoutubeDL({"outtmpl":"video.mp4"}).download([url])
    frames = len(os.listdir(FOLDER))
    if frames == 0:
        cli = ff(
            inputs = {"video.mp4":None},
            outputs = {FOLDER + "/frame-%1d.png":"-r " + str(args[2]) + " -format image2"}
        )
        cli.run()
    frames = len(os.listdir(FOLDER))
    size = 10
    cmin = 1
    cmax = size
    for i in range(40):
        figures.append(plt.figure(i))
        threads.append(RenderThread(range(cmin, cmax+1), figures[i]))
        threads[i].start()
        print("Thread " + str(i) + " started with frames from " + str(cmin) + " to " + str(cmax))
        cmin += size
        cmax += size

    while cmax < frames:
        for i in range(40):
            if (threads[i].finished):
                threads[i] = RenderThread(range(cmin, cmax+1), figures[i])
                threads[i].start()
                print("Thread " + str(i) + " restarted with frames from " + str(cmin) + " to " + str(cmax))
                cmin += size
                cmax += size
        sleep(0.5)

if __name__ == "__main__":
    main()


