ftp -niv << EOF
open $1
user guest guest
put "$2" $3/$2
EOF
