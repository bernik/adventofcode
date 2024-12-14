import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/option
import gleam/regexp
import gleam/string
import gleam/yielder as y
import pprint.{debug as d}
import simplifile
import utils

fn parse_input(file) {
  let assert Ok(content) = simplifile.read(file)
  let assert Ok(re) = regexp.from_string("-?\\d+")
  content
  |> string.split(on: "\n")
  |> list.map(fn(line) {
    let assert [x, y, dx, dy] =
      line
      |> regexp.scan(with: re)
      |> list.map(fn(m) {
        let regexp.Match(n, _) = m
        utils.string_to_int(n)
      })

    #(x, y, dx, dy)
  })
}

fn print_area(w, h, robots: List(#(Int, Int, Int, Int))) {
  let robots =
    robots
    |> list.fold(dict.new(), fn(acc, r) {
      let xy = #(r.0, r.1)
      acc
      |> dict.upsert(xy, fn(v) {
        case v {
          option.Some(n) -> n + 1
          option.None -> 1
        }
      })
    })

  let xmid = w / 2
  let ymid = h / 2

  list.range(0, h - 1)
  |> list.each(fn(y) {
    list.range(0, w - 1)
    |> list.map(fn(x) {
      case x == xmid || y == ymid {
        True -> " "
        False ->
          case dict.get(robots, #(x, y)) {
            Ok(n) -> int.to_string(n)
            _ -> "."
          }
      }
    })
    |> string.concat
    |> io.println
  })
}

fn move(robots, w, h, steps) {
  robots
  |> list.map(fn(robot) {
    let #(x, y, dx, dy) = robot
    let x = { { { x + dx * steps } % w } + w } % w
    let y = { { { y + dy * steps } % h } + h } % h
    #(x, y, dx, dy)
  })
}

fn part1(file, w, h) {
  parse_input(file)
  |> move(w, h, 100)
  |> list.fold(dict.new(), fn(acc, robot) {
    let #(x, y) = #(robot.0, robot.1)
    case x == w / 2 || y == h / 2 {
      True -> acc
      False -> {
        let k = #(x < { w / 2 }, y < { h / 2 })
        acc
        |> dict.upsert(k, fn(n) {
          case n {
            option.Some(n) -> n + 1
            _ -> 1
          }
        })
      }
    }
  })
  |> dict.values
  |> list.fold(1, int.multiply)
  |> int.to_string
}

fn part2(file) {
  let w = 101
  let h = 103

  let result =
    parse_input(file)
    |> y.iterate(move(_, w, h, 1))
    |> y.index
    |> y.drop_while(fn(state) {
      let #(robots, _) = state
      let total = list.length(robots) |> int.to_float

      robots
      |> list.fold(dict.new(), fn(acc, r) {
        let #(x, y, _, _) = r
        let k = #(x <= w / 2, y <= h / 2)

        dict.upsert(acc, k, fn(n) {
          case n {
            option.Some(n) -> n + 1
            _ -> 1
          }
        })
      })
      |> dict.values
      |> list.all(fn(n) { int.to_float(n) /. total <. 0.5 })
    })
    |> y.first()

  case result {
    Ok(#(state, index)) -> {
      io.print("\u{001b}[2J")
      io.println("==== Iteration: " <> int.to_string(index))
      print_area(w, h, state)

      int.to_string(index)
    }
    _ -> "not found"
  }
}

pub fn main() {
  // let file = "./resources/day14.example.txt"
  // let part1 = part1(file, 11, 7)
  let file = "./resources/day14.input.txt"
  let part1 = part1(file, 101, 103)
  let part2 = part2(file)

  io.println("Day 14 | Part 1: " <> part1)
  io.println("Day 14 | Part 2: " <> part2)
}
