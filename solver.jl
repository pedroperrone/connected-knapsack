using GLPKMathProgInterface
using JuMP

function parse_instance(file_name)
    f = open(file_name);
    lines = readlines(f)
    close(f)
    first_line = split(lines[1], " ")
    N = parse(Int64, first_line[1])
    w = parse(Float64, first_line[3])
    deleteat!(lines, 1)
    W = map((x) -> parse(Float64, x), filter((x) -> x != "", split(lines[1], " ")))
    deleteat!(lines, 1)
    V = map((x) -> parse(Float64, x), filter((x) -> x != "", split(lines[1], " ")))
    deleteat!(lines, 1)
    G = reshape(zeros(N * N), N, N)
    for line in lines
        l = map((x) -> parse(Int64, x) + 1, split(line, " "))
        G[l[1], l[2]] = 1
        G[l[2], l[1]] = 1
    end
    return (N, w, W, V, G)
end

function parse_output(S)
    solution = ""
    for i in N_range
        if s[i] == 1
            solution = solution * string(i-1) * " "
        end
    end
    return solution
end

M = typemax(Int16)
N, w, W, V, G = parse_instance(ARGS[2])
N_range = 1:N

m = Model(solver = GLPKSolverMIP());

@variable(m, S[N_range], Bin)
@variable(m, F[N_range, N_range] >= 0, Int)
@variable(m, O[N_range], Bin)

@objective(m, Max, sum(S[i] * V[i] for i = N_range))
@constraints(m, begin
    sum(S[i] * W[i] for i in N_range) <= w # (1)
    [i in N_range, j in N_range], F[i, j] <= G[i, j] * M # (2)
    [i in N_range, j in N_range], F[i, j] <= S[i] * M # (3)
    [i in N_range, j in N_range], F[i, j] <= S[j] * M # (4)
    sum(O[i] for i in N_range) == 1 # (5)
    [i in N_range], sum(F[i, j] for j in N_range) >= (sum(S[n] for n in N_range) - 1) - (1 - O[i]) * M # (6)
    [i in N_range], sum(F[i, j] for j in N_range) <= (sum(S[n] for n in N_range) - 1) # (7)
    [i in N_range], O[i] <= S[i] # (8)
    [j in N_range], sum(F[i, j] for i in N_range) <= (1 - O[j]) * M # (9)
    [i in N_range], sum(F[i, j] for j in N_range) + 1 <= sum(F[j, i] for j in N_range) + M * (O[i] + (1 - S[i])) # (10)
    [i in N_range], sum(F[i, j] for j in N_range) + 1 >= sum(F[j, i] for j in N_range) - M * (O[i] + (1 - S[i])) # (11)
    [i in N_range], F[i, i] == 0
end)
status = solve(m)
otm = getobjectivevalue(m)
s = getvalue(S)
solution = parse_output(s)
println("VALOR")
println(otm)
f = open(ARGS[1], "w")
write(f, solution)
close(f)

