import gleam/int
import gleam/string
import simplifile

pub fn read_lines(path) -> List(String) {
  let assert Ok(content) = simplifile.read(path)
  string.split(content, on: "\n")
}

pub fn string_to_int(s) {
  let assert Ok(n) = int.parse(s)
  n
}
