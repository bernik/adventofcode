import gleam/int
import gleam/io
import gleam/list
import gleam/regexp
import gleam/string
import pprint.{debug as d}
import simplifile

fn parse_input(file) {
  let assert Ok(content) = simplifile.read(file)
  let assert Ok(#(first, second)) = string.split_once(content, on: "\n\n")

  let patterns = first |> string.trim |> string.split(on: ", ")
  let designs = second |> string.split(on: "\n") |> list.map(string.trim)

  #(patterns, designs)
}

fn part1(file) {
  let #(patterns, designs) = parse_input(file)

  let assert Ok(re) =
    patterns
    |> string.join("|")
    |> fn(s) { "^(" <> s <> ")+$" }
    |> regexp.from_string

  designs
  |> list.filter(regexp.check(_, with: re))
  |> list.length
  |> int.to_string
}

fn part2(file) {
  "todo"
}

pub fn main() {
  let assert "6" = part1("./resources/day19.example.txt")
  let assert "16" = part2("./resources/day19.example.txt")

  let file = "./resources/day19.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)
  io.println("Day 19 | Part 1: " <> part1)
  io.println("Day 19 | Part 2: " <> part2)
}
