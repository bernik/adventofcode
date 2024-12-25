import gleam/int
import gleam/io
import gleam/list
import gleam/pair
import gleam/string
import simplifile

fn parse_input(file) {
  let assert Ok(content) = simplifile.read(file)

  string.split(content, on: "\n\n")
  |> list.map(fn(block) {
    let t = case string.first(block) {
      Ok("#") -> "lock"
      Ok(".") -> "key"
      _ -> panic as "invalid block"
    }
    let block =
      block
      |> string.split("\n")
      |> list.map(string.split(_, ""))
      |> list.transpose
      |> list.map(list.count(_, fn(x) { x == "#" }))
    #(t, block)
  })
  |> list.partition(fn(x) { pair.first(x) == "lock" })
  |> pair.map_first(list.map(_, pair.second))
  |> pair.map_second(list.map(_, pair.second))
}

fn part1(file) {
  let #(locks, keys) = parse_input(file)

  locks
  |> list.fold(0, fn(acc, lock) {
    keys
    |> list.map(list.zip(lock, _))
    |> list.count(list.all(_, fn(xs) {
      let #(a, b) = xs
      b <= 7 - a
    }))
    |> int.add(acc)
  })
  |> int.to_string
}

fn part2(_file) {
  "todo"
}

pub fn main() {
  // let file = "./resources/day25.example.txt"
  let file = "./resources/day25.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)

  io.println("Day 25 | Part 1: " <> part1)
  io.println("Day 25 | Part 2: " <> part2)
}
