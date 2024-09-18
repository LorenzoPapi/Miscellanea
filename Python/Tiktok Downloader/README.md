# Tiktok Downloader
Literally the title. Can take in input numerous links, or a file containing the list of links.
Works with every photo books and normal videos.

### Requirements
---
[Colorama](https://github.com/tartley/colorama) which can be installed with `pip install colorama`

[BeautifulSoup4](https://www.crummy.com/software/BeautifulSoup/) through `pip install beautifulsoup4`

[Requests](https://pypi.org/project/requests/) through `pip install requests`

### Using
---
```bash
python main.py -h # prints help 
python main.py links.txt # downloads all links inside the file, located in the same directory as the script
python main.py link1 link2 ... # downloads links taken from command line
```