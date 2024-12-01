import gleam/int
import gleam/io
import gleam/list
import gleam/regexp.{from_string, split}

import utils.{read_lines, string_to_int}

fn parse_lines(file) {
  read_lines(file)
  |> list.map(fn(line) {
    let assert Ok(whitespace) = from_string(" +")
    let assert [l, r] = split(line, with: whitespace)
    #(string_to_int(l), string_to_int(r))
  })
  |> list.fold(#([], []), fn(acc: #(List(Int), List(Int)), pair) {
    #([pair.0, ..acc.0], [pair.1, ..acc.1])
  })
}

fn part1(file) {
  let #(lefts, rights) = parse_lines(file)
  let lefts = list.sort(lefts, by: int.compare)
  let rights = list.sort(rights, by: int.compare)
  list.map2(lefts, rights, fn(left, right) { int.absolute_value(left - right) })
  |> list.fold(0, int.add)
  |> int.to_string
}

fn part2(file) {
  let #(lefts, rights) = parse_lines(file)
  lefts
  |> list.map(fn(l) { l * list.count(rights, fn(r) { r == l }) })
  |> list.fold(0, int.add)
  |> int.to_string
}

pub fn main() {
  let file = "./resources/day01.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)

  io.println("Day 01 | Part 1: " <> part1)
  io.println("Day 01 | Part 2: " <> part2)
}
