import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/yielder
import utils

fn next_number(n) {
  let m = 16_777_216 - 1

  let a =
    n
    |> int.bitwise_shift_left(6)
    |> int.bitwise_exclusive_or(n)
    |> int.bitwise_and(m)

  let b =
    a
    |> int.bitwise_shift_right(5)
    |> int.bitwise_exclusive_or(a)
    |> int.bitwise_and(m)

  let c =
    b
    |> int.bitwise_shift_left(11)
    |> int.bitwise_exclusive_or(b)
    |> int.bitwise_and(m)

  c
}

fn part1(file) {
  utils.read_lines(file)
  |> list.map(fn(n) {
    n
    |> utils.string_to_int
    |> yielder.iterate(next_number)
    |> yielder.drop(2000)
    |> yielder.first
  })
  |> result.values
  |> list.fold(0, int.add)
  |> int.to_string
}

fn part2(file) {
  "todo"
}

pub fn main() {
  // let assert "126384" = part1("./resources/day22.example.txt")

  let file = "./resources/day22.input.txt"
  // let file = "./resources/day22.example.txt"
  let part1 = part1(file)
  io.println("Day 22 | Part 1: " <> part1)

  let part2 = part2(file)
  io.println("Day 22 | Part 2: " <> part2)
}
