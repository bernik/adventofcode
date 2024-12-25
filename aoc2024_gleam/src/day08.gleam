import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/option.{None, Some}
import gleam/pair
import gleam/result
import gleam/string
import gleam/yielder
import utils.{read_lines}

fn parse_input(file) {
  read_lines(file)
  |> list.index_map(fn(line, row) {
    string.split(line, on: "")
    |> list.index_map(fn(x, col) { #(#(row, col), x) })
  })
  |> list.flatten
  |> dict.from_list
}

/// for debug
// fn print_map(map) {
//   let height =
//     map
//     |> dict.keys
//     |> list.map(pair.first(_))
//     |> list.reduce(int.max)
//     |> result.unwrap(0)

//   list.range(0, height)
//   |> list.each(fn(row) {
//     map
//     |> dict.to_list
//     |> list.filter(fn(x) { x.0.0 == row })
//     |> list.sort(fn(a, b) { int.compare(a.0.1, b.0.1) })
//     |> list.map(pair.second)
//     |> string.concat
//     |> io.println
//   })

//   map
// }

fn antennas(map) {
  map
  |> dict.to_list
  |> list.fold(dict.new(), fn(acc, antenna) {
    let #(pos, name) = antenna
    case name {
      "." -> acc
      _ ->
        dict.upsert(acc, name, fn(x) {
          case x {
            Some(xs) -> [pos, ..xs]
            None -> [pos]
          }
        })
    }
  })
}

fn height(map) {
  map
  |> dict.keys
  |> list.map(pair.first(_))
  |> list.reduce(int.max)
  |> result.unwrap(0)
}

fn width(map) {
  map
  |> dict.keys
  |> list.map(pair.second(_))
  |> list.reduce(int.max)
  |> result.unwrap(0)
}

fn antinodes(
  a: #(Int, Int),
  b: #(Int, Int),
  width w: Int,
  height h: Int,
  all all: Bool,
) -> List(#(Int, Int)) {
  let assert [#(row1, col1), #(row2, col2)] =
    [a, b] |> list.sort(fn(a, b) { int.compare(a.0, b.0) })

  let dr = row2 - row1
  let dc = col2 - col1

  let next =
    #(row2 + dr, col2 + dc)
    |> yielder.iterate(fn(x) { #(x.0 + dr, x.1 + dc) })
    |> yielder.take_while(fn(pos) {
      { 0 <= pos.0 && pos.0 <= h } && { 0 <= pos.1 && pos.1 <= w }
    })
    |> yielder.to_list

  let prev =
    #(row1 - dr, col1 - dc)
    |> yielder.iterate(fn(x) { #(x.0 - dr, x.1 - dc) })
    |> yielder.take_while(fn(pos) {
      { 0 <= pos.0 && pos.0 <= h } && { 0 <= pos.1 && pos.1 <= w }
    })
    |> yielder.to_list

  case all {
    True -> list.append(prev, next)
    False -> list.append(list.take(prev, 1), list.take(next, 1))
  }
}

fn part1(file) {
  let map = parse_input(file)
  let w = width(map)
  let h = height(map)

  antennas(map)
  |> dict.fold([], fn(acc, _k, antennas) {
    antennas
    |> list.combination_pairs
    |> list.flat_map(fn(x) {
      antinodes(x.0, x.1, width: w, height: h, all: False)
    })
    |> list.append(acc)
  })
  |> list.unique
  |> list.length
  |> int.to_string
}

fn part2(file) {
  let map = parse_input(file)
  let w = width(map)
  let h = height(map)

  antennas(map)
  |> dict.fold([], fn(acc, _k, antennas) {
    antennas
    |> list.combination_pairs
    |> list.flat_map(fn(x) {
      antinodes(x.0, x.1, width: w, height: h, all: True)
      |> list.append([x.0, x.1])
    })
    |> list.append(acc)
  })
  |> list.unique
  |> list.length
  |> int.to_string
}

pub fn main() {
  let file = "./resources/day08.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)

  io.println("Day 08 | Part 1: " <> part1)
  io.println("Day 08 | Part 2: " <> part2)
}
