import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string
import utils.{read_lines}

fn parse_input(file) {
  read_lines(file)
  |> list.index_map(fn(line, row) {
    line
    |> string.split(on: "")
    |> list.index_map(fn(c, col) { #(#(row, col), c) })
  })
  |> list.flatten
  |> dict.from_list
}

fn word(m, from: #(Int, Int), shift: #(Int, Int)) {
  list.range(0, 3)
  |> list.map(fn(n) { #(from.0 + n * shift.0, from.1 + n * shift.1) })
  |> list.map(dict.get(m, _))
  |> result.values
  |> string.concat
}

fn xmas_count(m, from) {
  list.combination_pairs([-1, 0, 1, 1, -1])
  |> list.map(fn(x) { [x.0, x.1] })
  |> list.flat_map(list.permutations(_))
  |> list.unique
  |> list.map(fn(x) {
    case x {
      [a, b] -> #(a, b)
      _ -> panic
    }
  })
  |> list.map(word(m, from, _))
  |> list.filter(fn(w) { w == "XMAS" })
  |> list.length
}

fn part1(file) {
  let m = parse_input(file)
  m
  |> dict.filter(fn(_, v) { v == "X" })
  |> dict.keys
  |> list.map(xmas_count(m, _))
  |> int.sum
  |> int.to_string
}

fn is_xmax_puzzle(m, from: #(Int, Int)) {
  let mas1 = [#(from.0 - 1, from.1 - 1), from, #(from.0 + 1, from.1 + 1)]
  let mas2 = [#(from.0 + 1, from.1 - 1), from, #(from.0 - 1, from.1 + 1)]
  let word1 = mas1 |> list.map(dict.get(m, _)) |> result.values |> string.concat
  let word2 = mas2 |> list.map(dict.get(m, _)) |> result.values |> string.concat

  [word1, word2] |> list.all(list.contains(["MAS", "SAM"], _))
}

fn part2(file) {
  let m = parse_input(file)
  m
  |> dict.filter(fn(_, v) { v == "A" })
  |> dict.keys
  |> list.filter(is_xmax_puzzle(m, _))
  |> list.length
  |> int.to_string
}

pub fn main() {
  let file = "./resources/day04.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)
  io.println("Day 04 | Part 1: " <> part1)
  io.println("Day 04 | Part 2: " <> part2)
}
