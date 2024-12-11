import gleam/dict.{type Dict}
import gleam/int
import gleam/io
import gleam/list
import gleam/pair
import gleam/result
import gleam/string
import simplifile
import utils

fn parse_input(file) {
  let assert Ok(content) = simplifile.read(file)
  content |> string.split(on: " ") |> list.map(utils.string_to_int)
}

fn split_number(n) {
  let s = int.to_string(n)
  let half = string.length(s) / 2
  let left = string.slice(s, 0, half) |> utils.string_to_int
  let right = string.slice(s, half, half) |> utils.string_to_int
  #(left, right)
}

fn is_even_digits(n) {
  int.to_string(n)
  |> string.length
  |> int.modulo(2)
  |> result.unwrap(0)
  |> int.is_even
}

fn stones_count(n, steps, memo: Dict(#(Int, Int), Int)) {
  case dict.get(memo, #(n, steps)) {
    Ok(res) -> #(res, memo)
    _ -> {
      let divisible = is_even_digits(n)
      case steps {
        0 -> #(1, memo |> dict.insert(#(n, steps), 1))
        _ -> {
          case n {
            0 -> {
              let #(res, memo) = stones_count(1, steps - 1, memo)
              #(res, memo |> dict.insert(#(n, steps), res))
            }
            _ if divisible -> {
              let #(a, b) = split_number(n)
              let #(res1, memo1) = stones_count(a, steps - 1, memo)
              let #(res2, memo2) =
                stones_count(b, steps - 1, dict.merge(memo, memo1))
              #(res1 + res2, memo2 |> dict.insert(#(n, steps), res1 + res2))
            }
            _ -> {
              let #(res, memo) = stones_count(n * 2024, steps - 1, memo)
              #(res, memo |> dict.insert(#(n, steps), res))
            }
          }
        }
      }
    }
  }
}

fn solve(file, steps) {
  parse_input(file)
  |> list.fold(#(0, dict.new()), fn(acc, n) {
    let #(acc, memo) = acc
    let #(res, memo1) = stones_count(n, steps, memo)
    #(acc + res, memo1 |> dict.insert(#(n, steps), res) |> dict.merge(memo))
  })
  |> pair.first
  |> int.to_string
}

pub fn main() {
  let file = "./resources/day11.input.txt"
  let part1 = solve(file, 25)
  let part2 = solve(file, 75)

  io.println("Day 11 | Part 1: " <> part1)
  io.println("Day 11 | Part 2: " <> part2)
}
