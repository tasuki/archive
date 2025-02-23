#!/bin/bash

wget --mirror --convert-links --adjust-extension --page-requisites \
     --span-hosts --domains=tasuki.org,lsg.go.art.pl \
     --directory-prefix=archive https://tasuki.org/

# TODO fix the domains for intersite links

# TODO wget appends .html to suffixless urls
#      check and/or fix that for intersite links
