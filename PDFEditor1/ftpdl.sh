ftp -niv << EOF
open 127.0.0.1
user guest guest
get "$1"
EOF
