import gleam/int
import gleam/io
import gleam/list
import gleam/option.{Some}
import gleam/pair
import gleam/regexp.{Match}
import simplifile
import utils.{string_to_int}

pub fn part1(file) {
  let assert Ok(content) = simplifile.read(file)
  let assert Ok(re) = regexp.from_string("mul\\((\\d+),(\\d+)\\)")
  let matches = regexp.scan(re, content)
  matches
  |> list.map(fn(m: regexp.Match) {
    let assert [a, b] =
      m.submatches
      |> option.values
      |> list.map(utils.string_to_int)
    a * b
  })
  |> list.fold(0, int.add)
  |> int.to_string
}

pub fn part2(file) {
  let assert Ok(content) = simplifile.read(file)
  let assert Ok(re) =
    regexp.from_string("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)")
  let matches = regexp.scan(re, content)
  matches
  |> list.fold(#(True, 0), fn(acc, m) {
    let #(enabled, res) = acc
    case m {
      Match("do()", _) -> #(True, res)
      Match("don't()", _) -> #(False, res)
      Match(_, [Some(a), Some(b)]) ->
        case enabled {
          True -> #(enabled, res + { string_to_int(a) * string_to_int(b) })
          False -> #(enabled, res)
        }
      _ -> panic as "Invalid match"
    }
  })
  |> pair.second
  |> int.to_string
}

pub fn main() {
  let file = "./resources/day03.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)

  io.println("Day 02 | Part 1: " <> part1)
  io.println("Day 02 | Part 2: " <> part2)
}
