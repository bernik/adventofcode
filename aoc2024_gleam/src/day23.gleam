import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/order
import gleam/pair
import gleam/result
import gleam/set
import gleam/string
import utils

fn parse_lines(file) {
  utils.read_lines(file)
  |> list.map(fn(line) {
    let assert Ok(#(l, r)) = string.split_once(line, on: "-")
    case string.compare(l, r) {
      order.Lt | order.Eq -> #(l, r)
      _ -> #(r, l)
    }
  })
}

fn part1(file) {
  let lines = parse_lines(file)
  let set =
    lines
    |> list.flat_map(fn(x) { [x, pair.swap(x)] })
    |> set.from_list

  lines
  |> list.sort(fn(a, b) {
    case string.compare(a.0, b.0) {
      order.Eq -> string.compare(a.1, b.1)
      o -> o
    }
  })
  |> list.group(pair.first)
  |> dict.map_values(fn(_, xs) { list.map(xs, pair.second) })
  |> dict.to_list
  |> list.flat_map(fn(x) {
    let #(first, xs) = x

    xs
    |> list.combination_pairs
    |> list.filter_map(fn(pair) {
      case set.contains(set, pair) {
        True -> Ok(#(first, pair.0, pair.1))
        _ -> Error(Nil)
      }
    })
  })
  |> list.count(fn(x) {
    [x.0, x.1, x.2] |> list.any(string.starts_with(_, "t"))
  })
  |> int.to_string
}

fn bron_kerbosch(r, p, x, g) {
  case set.is_empty(p) && set.is_empty(x) {
    True -> [r]
    _ -> {
      list.fold(set.to_list(p), [], fn(acc, v) {
        let nodes = dict.get(g, v) |> result.unwrap(set.new())
        acc
        |> list.append(bron_kerbosch(
          set.insert(r, v),
          set.intersection(p, nodes),
          set.intersection(x, nodes),
          g,
        ))
      })
    }
  }
}

fn part2(file) {
  let lines = parse_lines(file)

  let all_conns =
    dict.merge(list.group(lines, pair.first), list.group(lines, pair.second))
    |> dict.map_values(fn(k, v) {
      list.map(v, fn(x) {
        case x {
          #(a, b) if a == k -> b
          #(a, b) if b == k -> a
          _ -> panic as "huh"
        }
      })
      |> set.from_list
    })

  bron_kerbosch(
    set.new(),
    dict.keys(all_conns) |> set.from_list,
    set.new(),
    all_conns,
  )
  |> list.sort(fn(a, b) { int.compare(set.size(a), set.size(b)) })
  |> list.last
  |> result.unwrap(set.new())
  |> set.to_list
  |> list.sort(string.compare)
  |> string.join(",")
}

pub fn main() {
  // let file = "./resources/day23.example.txt"
  let file = "./resources/day23.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)

  io.println("Day 23 | Part 1: " <> part1)
  io.println("Day 23 | Part 2: " <> part2)
}
