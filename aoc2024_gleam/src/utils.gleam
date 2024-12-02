import gleam/int
import gleam/list
import gleam/string
import simplifile

pub fn read_lines(path) -> List(String) {
  let assert Ok(content) = simplifile.read(path)
  content |> string.trim |> string.split(on: "\n") |> list.map(string.trim)
}

pub fn string_to_int(s) {
  let assert Ok(n) = int.parse(s)
  n
}
