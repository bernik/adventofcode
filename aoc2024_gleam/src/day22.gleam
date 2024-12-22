import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/option
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

fn seqs(n, lenght) {
  yielder.iterate(n, next_number)
  |> yielder.map(fn(n) { n % 10 })
  |> yielder.take(lenght)
  |> yielder.to_list
  |> list.window(5)
  |> list.fold(dict.new(), fn(acc, w) {
    let seq = w |> list.window_by_2 |> list.map(fn(w) { w.1 - w.0 })
    let value = list.last(w) |> result.unwrap(0)

    acc
    |> dict.upsert(seq, fn(x) {
      case x {
        option.Some(v) -> v
        option.None -> value
      }
    })
  })
  |> dict.to_list
}

fn part2(file) {
  utils.read_lines(file)
  |> list.map(utils.string_to_int)
  |> list.flat_map(seqs(_, 2001))
  |> list.fold(dict.new(), fn(acc, x) {
    let #(seq, v) = x
    acc
    |> dict.upsert(seq, fn(vv) { vv |> option.unwrap(0) |> int.add(v) })
  })
  |> dict.values
  |> list.sort(int.compare)
  |> list.last
  |> result.unwrap(0)
  |> int.to_string
}

pub fn main() {
  // let file = "./resources/day22.example.txt"
  let file = "./resources/day22.input.txt"
  let part1 = part1(file)
  io.println("Day 22 | Part 1: " <> part1)

  let part2 = part2(file)
  io.println("Day 22 | Part 2: " <> part2)
}
