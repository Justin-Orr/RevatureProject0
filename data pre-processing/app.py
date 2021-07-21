import csv

def flip(s):
  new_str_array = s.split(", ")
  if len(new_str_array) > 1:
    t = new_str_array[1] + ' ' + new_str_array[0]
    return t
  elif s == '':
    return 'N/A'
  else:
    return s

def write_to_file(s):
  with open('books_updated.csv', mode='a', newline='') as updated_file:
    book_writer = csv.writer(updated_file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
    for entry in s:
      book_writer.writerow(entry)

with open('books.csv') as csv_file:
  csv_reader = csv.reader(csv_file, delimiter=',')
  s = []
  line_num = 0
  for row in csv_reader:
    t = []
    for token in row:
      t.append(flip(token))
    s.append(t)
  write_to_file(s)