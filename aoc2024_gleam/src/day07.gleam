import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string
import utils.{read_lines, string_to_int}

fn parse_input(file) {
  read_lines(file)
  |> list.map(fn(line) {
    let assert [total, parts] = string.split(line, on: ": ")
    let parts = parts |> string.split(on: " ") |> list.map(string_to_int)
    #(string_to_int(total), parts)
  })
}

fn try_math(args, acc, total, path, with_concat) {
  case args {
    [] ->
      case acc == total {
        True -> Ok(acc)
        _ -> Error(Nil)
      }
    [arg, ..args] -> {
      case acc > total, with_concat {
        True, _ -> Error(Nil)
        False, False -> {
          let acc1 = acc + arg
          let acc2 = acc * arg
          use <- result.lazy_or(try_math(
            args,
            acc1,
            total,
            ["add", ..path],
            with_concat,
          ))
          try_math(args, acc2, total, ["mul", ..path], with_concat)
        }
        False, True -> {
          let acc1 = acc + arg
          let acc2 = acc * arg
          let acc3 = string_to_int(int.to_string(acc) <> int.to_string(arg))
          use <- result.lazy_or(try_math(
            args,
            acc1,
            total,
            ["add", ..path],
            with_concat,
          ))
          use <- result.lazy_or(try_math(
            args,
            acc2,
            total,
            ["mul", ..path],
            with_concat,
          ))
          try_math(args, acc3, total, ["con", ..path], with_concat)
        }
      }
    }
  }
}

fn solve(file, with_concat) {
  parse_input(file)
  |> list.filter_map(fn(row) {
    let assert #(total, [a, ..args]) = row
    try_math(args, a, total, [], with_concat)
  })
  |> int.sum
  |> int.to_string
}

pub fn main() {
  let file = "./resources/day07.input.txt"
  let part1 = solve(file, False)
  let part2 = solve(file, True)

  io.println("Day 06 | Part 1: " <> part1)
  io.println("Day 06 | Part 2: " <> part2)
}
