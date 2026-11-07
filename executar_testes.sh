#!/usr/bin/env bash
set -e

echo "----------------------------------------------------"
echo "1. Compilando os arquivos Java do projeto..."
echo "----------------------------------------------------"
javac -encoding UTF-8 *.java
echo "Compilacao concluida com sucesso!"
echo ""

echo "----------------------------------------------------"
echo "2. Executando os testes automatizados (TesteJogo)..."
echo "----------------------------------------------------"
java TesteJogo
