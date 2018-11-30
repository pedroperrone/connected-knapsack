## O Repositório

O conteúdo deste repositório é a implementação da meta-heurística Busca Tabu e da formulação de um Programa Inteiro para a solução ou aproximação da solução do Problema da Mochila Conexa. Mais informações a respeito do problema e dos algoritmos implementados podem ser encontrados no relatório.

## Dependências

* kotlinc - Compilação do código da meta-heurística
* java - Execução do programa da meta-heurística
* Julia Lang - Execução do Programa Inteiro com o uso do solver genérico GLPK

## Execução

### Meta-heurística

Primeiramente é necessário compilar o projeto. Isso pode ser feito navegando pelo terminal até o diretório do projeto e usando o comando `./compile`. O resultado é a geração do arquivo `tabu-search.jar`. A ordem de parâmetros para a execução do programa é: `arquivo_de_saida`, `arquivo_de_entrada`, `tabu_rate`, `iterations_rate` e `random_seed`. Um exemplo de execução é a seguinte:

```
java -jar tabu-search.jar output moc/moc01 0.1 5 2345
```

### Programa Inteiro

Após a certificação de que as bibliotecas `GLPKMathProgInterface` e `JuMP` estão intaladas, basta rodar o arquivo `solver.jl`. A ordem dos parâmetros é: `arquivo_de_saida` e `arquivo_de_entrada`. Segue um exemplo de execução:

```
julia solver.jl output moc/moc01
```
