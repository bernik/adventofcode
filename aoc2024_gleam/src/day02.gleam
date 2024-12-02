import gleam/int
import gleam/io
import gleam/list
import gleam/string
import utils.{read_lines, string_to_int}

fn parse_lines(file) {
  read_lines(file)
  |> list.map(fn(line) { string.split(line, " ") |> list.map(string_to_int) })
}

fn is_valid_line(line) {
  let diffs = list.window_by_2(line) |> list.map(fn(ab) { ab.0 - ab.1 })
  let is_inc = list.all(diffs, fn(d) { d < 0 })
  let is_dec = list.all(diffs, fn(d) { d > 0 })
  let is_valid_step = list.all(diffs, fn(d) { -3 <= d && d <= 3 })
  { is_inc || is_dec } && is_valid_step
}

pub fn part1(file) {
  parse_lines(file)
  |> list.filter(is_valid_line)
  |> list.length
  |> int.to_string
}

fn drop_by_index(l, i) {
  l
  |> list.index_fold([], fn(acc, item, index) {
    case index {
      idx if i == idx -> acc
      _ -> [item, ..acc]
    }
  })
  |> list.reverse
}

fn variants(line) {
  list.range(0, list.length(line) - 1)
  |> list.map(drop_by_index(line, _))
}

pub fn part2(file) {
  parse_lines(file)
  |> list.filter(fn(line) {
    is_valid_line(line) || list.any(variants(line), is_valid_line)
  })
  |> list.length
  |> int.to_string
}

pub fn main() {
  let file = "./resources/day02.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)

  io.println("Day 02 | Part 1: " <> part1)
  io.println("Day 02 | Part 2: " <> part2)
}
