import os
import subprocess
from functools import reduce
def main():
	all_files = subprocess.run(['dir', '*.java', '/S', '/B'], stdout=subprocess.PIPE)
	print(
		'{'+
		reduce(
			lambda acc,curr: acc+',\n'+curr,
			all_files.readlines()
		)
		+'}'
	)
	for j_file in f.readlines():
		print(str(os.path.basename(j_file)))


if __name__ =='__main__':
	main()
