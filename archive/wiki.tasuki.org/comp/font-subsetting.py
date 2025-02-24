# sudo apt install python3-fonttools

from fontTools import subset
from pathlib import Path

home = Path.home()

fonts = {
    f"{home}/Downloads/ArrowType-Recursive-1.085/Recursive_Web/woff2_static/RecursiveMonoCslSt-Regular.woff2":
        f"{home}/data/prog/continugo/media/rec-mono-csl-regular.woff2",
    f"{home}/Downloads/ArrowType-Recursive-1.085/Recursive_Web/woff2_static/RecursiveMonoCslSt-Italic.woff2":
        f"{home}/data/prog/continugo/media/rec-mono-csl-italic.woff2",
    f"{home}/Downloads/ArrowType-Recursive-1.085/Recursive_Web/woff2_static/RecursiveMonoCslSt-Bold.woff2":
        f"{home}/data/prog/continugo/media/rec-mono-csl-bold.woff2",
}

# basic latin, left/right angle quotation marks, curly apostrophe
limit = "--unicodes=0020-007F, 2039-203A, 00D7"

for src, dst in fonts.items():
    args = [
        src,
        limit,
        "--no-layout-closure",
        f"--output-file={dst}",
        "--flavor=woff2",
    ]
    subset.main(args)
