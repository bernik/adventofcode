import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/option
import gleam/order
import gleam/result
import gleam/set
import gleam/string
import pprint.{debug as d}
import utils

fn parse_input(file) {
  utils.read_lines(file)
  |> list.map(fn(line) {
    let assert Ok(#(x, y)) = string.split_once(line, on: ",")
    #(utils.string_to_int(x), utils.string_to_int(y))
  })
}

fn neighbours(pos: #(Int, Int), size) {
  [#(0, 1), #(0, -1), #(1, 0), #(-1, 0)]
  |> list.map(fn(x) { #(pos.0 + x.0, pos.1 + x.1) })
  |> list.filter(fn(x) {
    0 <= x.0 && x.0 <= { size - 1 } && 0 <= x.1 && x.1 <= { size - 1 }
  })
}

fn shortest_path(corrupted, queue, dists, visited, size) {
  case queue {
    [] -> dists
    [x, ..] if x == #(0, 0) -> dists
    [x, ..queue] -> {
      let neighbours =
        neighbours(x, size)
        |> list.filter(fn(x) {
          !set.contains(visited, x) && !set.contains(corrupted, x)
        })

      let assert Ok(cur_dist) = dists |> dict.get(x)

      let dists =
        neighbours
        |> list.fold(dists, fn(acc, x) {
          let n_dist = cur_dist + 1
          acc
          |> dict.upsert(x, fn(v) {
            case v {
              option.Some(vv) if n_dist < vv -> n_dist
              option.Some(vv) -> vv
              option.None -> n_dist
            }
          })
        })

      shortest_path(
        corrupted,
        queue
          |> list.append(neighbours)
          |> list.sort(fn(a, b) {
            let assert Ok(ad) = dict.get(dists, a)
            let assert Ok(bd) = dict.get(dists, b)
            case int.compare(ad, bd) {
              order.Eq -> int.compare(a.0 + a.1, b.0 + b.1)
              o -> o
            }
          })
          |> list.filter(fn(x) {
            !set.contains(visited, x) && !set.contains(corrupted, x)
          }),
        dists,
        set.insert(visited, x),
        size,
      )
    }
  }
}

fn print_map(size, visited, corrupted) {
  io.println(string.repeat("-", size))
  list.range(0, size - 1)
  |> list.each(fn(y) {
    list.range(0, size - 1)
    |> list.map(fn(x) {
      case set.contains(visited, #(x, y)) {
        True -> "0"
        False ->
          case set.contains(corrupted, #(x, y)) {
            True -> "#"
            False -> "."
          }
      }
    })
    |> string.concat
    |> io.println
  })
  io.println(string.repeat("-", size))
}

fn part1(file, size, take) {
  let corrupted = parse_input(file) |> list.take(take) |> set.from_list

  shortest_path(
    corrupted,
    [#(size - 1, size - 1)],
    dict.from_list([#(#(size - 1, size - 1), 0)]),
    set.new(),
    size,
  )
  |> dict.get(#(0, 0))
  |> result.unwrap(0)
  |> int.to_string
}

fn is_reachable(corrupted, queue, visited, size) {
  let end = #(size - 1, size - 1)
  case queue {
    [] -> False
    [x, ..] if x == end -> True
    [x, ..queue] -> {
      let neighbours =
        neighbours(x, size)
        |> list.filter(fn(n) {
          !set.contains(corrupted, n) && !set.contains(visited, n)
        })

      is_reachable(
        corrupted,
        neighbours |> list.append(queue),
        set.insert(visited, x),
        size,
      )
    }
  }
}

fn part2(file, size, from) {
  let input = parse_input(file)

  let start = #(0, 0)
  let visited = set.new()

  let assert Ok(i) =
    list.range(from, list.length(input))
    |> list.drop_while(fn(take) {
      is_reachable(
        input |> list.take(take) |> set.from_list,
        [start],
        visited,
        size,
      )
    })
    |> list.first

  let assert Ok(#(x, y)) = input |> list.drop(i - 1) |> list.first
  [x, y] |> list.map(int.to_string) |> string.join(",")
}

pub fn main() {
  let assert "22" = part1("./resources/day18.example.txt", 7, 12)
  let assert "6,1" = part2("./resources/day18.example.txt", 7, 12)

  let file = "./resources/day18.input.txt"
  let part1 = part1(file, 71, 1024)
  let part2 = part2(file, 71, 1024)
  io.println("Day 18 | Part 1: " <> part1)
  io.println("Day 18 | Part 2: " <> part2)
}
