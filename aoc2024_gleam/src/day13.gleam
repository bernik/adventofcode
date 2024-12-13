import gleam/int
import gleam/io
import gleam/list
import gleam/regexp
import gleam/string
import simplifile
import utils

fn parse_input(file) {
  let assert Ok(content) = simplifile.read(file)
  content
  |> string.split(on: "\n\n")
  |> list.map(fn(machine) {
    let assert Ok(re) = regexp.from_string("\\d+")
    regexp.scan(re, machine)
    |> list.map(fn(m) {
      let regexp.Match(s, _) = m
      utils.string_to_int(s)
    })
  })
}

fn solve(file, offset) {
  parse_input(file)
  |> list.map(fn(machine) {
    let assert [ax, ay, bx, by, x, y] = machine
    let x = x + offset
    let y = y + offset
    let d = ax * by - ay * bx
    case d {
      0 -> 0
      _ -> {
        let a = { x * by - y * bx } / d
        let b = { ax * y - ay * x } / d
        let tokens = 3 * a + b

        case a * ax + b * bx == x && a * ay + b * by == y {
          True -> tokens
          False -> 0
        }
      }
    }
  })
  |> list.filter(fn(n) { n != 0 })
  |> list.fold(0, int.add)
  |> int.to_string
}

pub fn main() {
  // let file = "./resources/day13.example.txt"
  let file = "./resources/day13.input.txt"
  let part1 = solve(file, 0)
  let part2 = solve(file, 10_000_000_000_000)

  io.println("Day 13 | Part 1: " <> part1)
  io.println("Day 13 | Part 2: " <> part2)
}
