import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/pair
import gleam/result
import gleam/set
import gleam/string
import utils

fn parse_input(file) {
  utils.read_lines(file)
  |> list.index_map(fn(line, row) {
    line
    |> string.split(on: "")
    |> list.index_map(fn(x, col) { #(#(row, col), x) })
  })
  |> list.flatten
  |> dict.from_list
}

fn find_start(map) {
  map
  |> dict.filter(fn(_, v) { v == "^" })
  |> dict.keys
  |> list.first
}

type Direction {
  Up
  Down
  Left
  Right
}

type Position =
  #(Int, Int)

fn turn_right(from) {
  case from {
    Up -> Right
    Down -> Left
    Left -> Up
    Right -> Down
  }
}

fn move(from: Position, direction: Direction) -> Position {
  case direction {
    Up -> #(from.0 - 1, from.1)
    Down -> #(from.0 + 1, from.1)
    Left -> #(from.0, from.1 - 1)
    Right -> #(from.0, from.1 + 1)
  }
}

fn find_path(current, direction, visited, map) {
  case set.contains(visited, #(current, direction)) {
    True -> Error("infinite loop")
    False -> {
      let visited = set.insert(visited, #(current, direction))
      let next = move(current, direction)
      case dict.get(map, next) {
        Ok("#") -> find_path(current, turn_right(direction), visited, map)
        Ok(_) -> find_path(next, direction, visited, map)
        Error(Nil) -> Ok(visited)
      }
    }
  }
}

fn part1(file) {
  let map = parse_input(file)
  let assert Ok(start) = find_start(map)
  let assert Ok(visited) = find_path(start, Up, set.new(), map)
  visited |> set.map(pair.first) |> set.size |> int.to_string
}

fn part2(file) {
  let map = parse_input(file)
  let assert Ok(start) = find_start(map)
  let assert Ok(visited) = find_path(start, Up, set.new(), map)
  visited
  |> set.map(pair.first)
  |> set.to_list
  |> list.filter(fn(pos) { dict.get(map, pos) |> result.unwrap("") == "." })
  |> list.filter(fn(pos) {
    find_path(start, Up, set.new(), dict.insert(map, pos, "#"))
    |> result.is_error
  })
  |> list.length
  |> int.to_string
}

pub fn main() {
  let file = "./resources/day06.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)

  io.println("Day 06 | Part 1: " <> part1)
  io.println("Day 06 | Part 2: " <> part2)
}
