import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Main {
   private static String GetInput() {
      try {
         // Lendo arquivo de texto e armazenando valor de cada linha
         File myObj = new File("input.txt");

         Scanner myReader = new Scanner(myObj);

         String data = "";

         while (myReader.hasNextLine()) {
            data += myReader.nextLine() + "\n";
         }

         myReader.close();

         return data;
      } catch (FileNotFoundException e) {
         e.printStackTrace();

         return "";
      }
   }

   // Função recursiva para printar o caminho
   private static void printPath(int[][] path, int v, int u, List<Integer> route)
   {
      if (path[v][u] == v) {
         return;
      }
      printPath(path, v, path[v][u], route);
      route.add(path[v][u]);
   }

   // Encontrando custo mínimo e chamando a função de print
   private static void printSolution(int[][] path, int[][] cost, int n, int start, int dest)
   {

      int minValue = cost[0][0];
      List<Integer> route = new ArrayList<>();

      int[] indexes = new int[2];
      for (int j = 0; j < cost.length; j++) {
         for (int i = 0; i < cost[j].length; i++) {
            if (cost[j][i] < minValue ) {
               minValue = cost[j][i];
               indexes[0] = j;
               indexes[1] = i;
            }
         }
      }

      for (int v = 0; v < n; v++)
      {
         for (int u = 0; u < n; u++)
         {
            if (u != v && path[v][u] != -1 && path[v][u] == path[indexes[0]][indexes[1]])
            {
               route.add(v);
               printPath(path, v, u, route);
               route.add(u);
            }
         }
      }
      System.out.printf("Menor caminho de %d —> %d é: %s\n", start, dest, route);
   }

   // Algoritmo de Floyd-Warshall
   public static void floydWarshall(int[][] adjMatrix)
   {
      if (adjMatrix ==null || adjMatrix.length == 0) {
         return;
      }

      int n = adjMatrix.length;

      int[][] cost = new int[n][n];
      int[][] path = new int[n][n];

      for (int v = 0; v < n; v++)
      {
         for (int u = 0; u < n; u++)
         {
            cost[v][u] = adjMatrix[v][u];

            if (v == u) {
               path[v][u] = 0;
            }
            else if (cost[v][u] != Integer.MAX_VALUE) {
               path[v][u] = v;
            }
            else {
               path[v][u] = -1;
            }
         }
      }

      // Rodando o algoritmo
      for (int k = 0; k < n; k++)
      {
         for (int v = 0; v < n; v++)
         {
            for (int u = 0; u < n; u++)
            {
               if (cost[v][k] != Integer.MAX_VALUE
                       && cost[k][u] != Integer.MAX_VALUE
                       && (cost[v][k] + cost[k][u] < cost[v][u]))
               {
                  cost[v][u] = cost[v][k] + cost[k][u];
                  path[v][u] = path[k][u];
               }
            }

            // Verificando elementos negativos
            if (cost[v][v] < 0)
            {
               System.out.println("Ciclo de peso negativo !!");
               return;
            }
         }
      }

      // Printando solução -> caminho, custo, vertice origem, vertice destino
      printSolution(path, cost, n, 4, 2);
   }

   public static void main(String[] args) {
      String[] inputRows = GetInput().split("\n");

      // Lendo dados do txt
      if (inputRows.length > 0) {
         Boolean directed = inputRows[0].equals("S");
         int vertexCount = Integer.parseInt(inputRows[1]);
         Vector<String> columns = new Vector<String>(vertexCount);
         Vector<String> rows = new Vector<String>(vertexCount);
         int matrix[][] = new int[vertexCount][vertexCount];
         int[][] adjMatrix = new int[vertexCount][vertexCount];

         for (int rowIndex = 2; rowIndex < inputRows.length; rowIndex++) {
            if (!inputRows[rowIndex].contains(",")) {
               columns.add(inputRows[rowIndex]);
               rows.add(inputRows[rowIndex]);
            } else {
               String[] rowValues = inputRows[rowIndex].split(", ");

               int matrixColumnI = columns.indexOf(rowValues[1]);
               int matrixRowI = rows.indexOf(rowValues[0]);

               matrix[matrixRowI][matrixColumnI] = Integer.parseInt(rowValues[2]);

               if (!directed) {
                  matrix[matrixColumnI][matrixRowI] = Integer.parseInt(rowValues[2]);
               }
            }
         }

         for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
               if (matrix[i][j] != 0) {
                  // Alimentando a matriz de conexões com a matriz de input (convertendo de Integer para int)
                  adjMatrix[i][j] = matrix[i][j];
               } else {
                  // Se for nulo, então recebe max value
                  adjMatrix[i][j] = Integer.MAX_VALUE;
               }
            }
         }

         // Run Floyd–Warshall algorithm
         floydWarshall(adjMatrix);
      }
   }
}