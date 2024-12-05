import gleam/bool
import gleam/int
import gleam/io
import gleam/list
import gleam/order
import gleam/result
import gleam/set
import gleam/string
import utils.{read_lines, string_to_int}

fn parse_input(file) {
  let lines = read_lines(file)
  let rules =
    list.take_while(lines, fn(l) { l != "" })
    |> list.map(fn(line) {
      line |> string.split(on: "|") |> list.map(string_to_int)
    })
    |> set.from_list

  let pages =
    list.drop(lines, set.size(rules) + 1)
    |> list.map(fn(line) {
      line |> string.split(on: ",") |> list.map(string_to_int)
    })

  #(rules, pages)
}

fn middle(line) {
  line |> list.drop(list.length(line) / 2) |> list.first
}

fn is_valid(line, rules) {
  line
  |> list.combination_pairs
  |> list.all(fn(pair) {
    let #(a, b) = pair
    set.contains(rules, [b, a]) |> bool.negate
  })
}

fn part1(file) {
  let #(rules, pages) = parse_input(file)
  pages
  |> list.filter(is_valid(_, rules))
  |> list.map(middle)
  |> result.values
  |> int.sum
  |> int.to_string
}

fn fix_order(line, rules) {
  list.sort(line, fn(a, b) {
    let lt = set.contains(rules, [a, b])
    let gt = set.contains(rules, [b, a])
    case lt, gt {
      True, _ -> order.Lt
      _, True -> order.Gt
      _, _ -> order.Eq
    }
  })
}

fn part2(file) {
  let #(rules, pages) = parse_input(file)
  pages
  |> list.filter(fn(line) { False == is_valid(line, rules) })
  |> list.map(fix_order(_, rules))
  |> list.map(middle)
  |> result.values
  |> int.sum
  |> int.to_string
}

pub fn main() {
  let file = "./resources/day05.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)

  io.println("Day 05 | Part 1: " <> part1)
  io.println("Day 05 | Part 2: " <> part2)
}
