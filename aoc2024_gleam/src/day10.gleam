import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/string
import utils

fn parse_input(file) {
  utils.read_lines(file)
  |> list.index_map(fn(line, row) {
    line
    |> string.split(on: "")
    |> list.index_map(fn(n, col) { #(#(row, col), utils.string_to_int(n)) })
  })
  |> list.flatten
  |> dict.from_list
}

fn possible_moves(map, from: #(Int, Int)) {
  let curr = dict.get(map, from)
  case curr {
    Ok(9) | Error(Nil) -> []
    Ok(curr) -> {
      [#(-1, 0), #(1, 0), #(0, -1), #(0, 1)]
      |> list.map(fn(shift) { #(from.0 + shift.0, from.1 + shift.1) })
      |> list.filter(fn(x) {
        case dict.get(map, x) {
          Ok(v) -> v == curr + 1
          _ -> False
        }
      })
    }
  }
}

fn score(from, map) {
  do_score([from], 0, map)
}

fn do_score(queue, result, map) {
  case queue {
    [] -> result
    [x, ..rest] -> {
      case dict.get(map, x) {
        Error(Nil) -> do_score(rest, result, map)
        Ok(9) -> do_score(rest, result + 1, map)
        _ -> {
          let possible_moves = possible_moves(map, x)
          let q = list.append(rest, possible_moves) |> list.unique
          do_score(q, result, map)
        }
      }
    }
  }
}

fn rating(start, map) {
  do_rating([[start]], map)
  |> list.unique
  |> list.length
}

fn do_rating(paths, map) {
  list.flat_map(paths, fn(path) {
    let assert Ok(head) = list.first(path)
    case dict.get(map, head) {
      Error(Nil) -> []
      Ok(9) -> [path]
      _ ->
        possible_moves(map, head)
        |> list.flat_map(fn(x) { do_rating([[x, ..path]], map) })
    }
  })
}

fn solve(file, result_fn) {
  let map = parse_input(file)
  map
  |> dict.filter(fn(_, v) { v == 0 })
  |> dict.keys
  |> list.map(fn(start) { result_fn(start, map) })
  |> list.fold(0, int.add)
  |> int.to_string
}

pub fn main() {
  let file = "./resources/day10.input.txt"
  let part1 = solve(file, score)
  let part2 = solve(file, rating)

  io.println("Day 10 | Part 1: " <> part1)
  io.println("Day 10 | Part 2: " <> part2)
}
