header="// Licensed Under GPL v3.0\n//https://github.com/14ercooper/14erEdit\n\n"

for file in $(find . -name '*.kt')
do
	sed -i -e "1i\
$header" $file
done
