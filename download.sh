#!/bin/bash

wget --mirror --convert-links --adjust-extension --page-requisites \
     --span-hosts --domains=tasuki.org,lsg.go.art.pl \
     --directory-prefix=archive https://tasuki.org/

for SBS in "enchiridion" "ttc"; do
    wget --convert-links --adjust-extension --page-requisites \
         --span-hosts --domains=tasuki.org \
         --directory-prefix=archive \
         "https://$SBS.tasuki.org/fonts/icons.svg" \
         "https://$SBS.tasuki.org/config.json"

    for FILE in $(jq -r '.files[]' "archive/$SBS.tasuki.org/config.json"); do
        wget --convert-links --adjust-extension --page-requisites \
             --span-hosts --domains=tasuki.org \
             --directory-prefix=archive \
             "https://$SBS.tasuki.org/$FILE"
    done

    sed -i 's|xlink:href="/|xlink:href="|g' \
        "archive/$SBS.tasuki.org/index.html"

    # yes I knew this would happen with the dynamic base url
    sed -E -i 's/=.\+"config.json"/="config.json"/' \
        "archive/$SBS.tasuki.org/scripts/all_min.js"

    # and yes I knew minifying would increase the suffering...
    sed -E -i 's/\.get\(.\+(.)\)\.then/.get(\1).then/' \
        "archive/$SBS.tasuki.org/scripts/all_min.js"
done
