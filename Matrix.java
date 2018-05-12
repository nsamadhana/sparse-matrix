//Nevan Samadhana
//pa3 Matrix ADT
//Student ID:1539153

public class Matrix{
  int nnz;
  int size;
  List [] row;

  public Matrix(int n){
    if(n<1){
      throw new RuntimeException(
        "Matrix() error: Attempting to make Matrix with size <1");
    }
    this.nnz = 0;
    this.size = n;
    row = new List[n+1];
    for(int i = 1; i<n+1;i++){
      row[i] = new List();
    }
  }

  private class Entry{
    int col;
    double val;

    //Constructor for entry
    private Entry(int col, double val){
      this.col = col;
      this.val = val;
    }

    public String toString(){
      return("("+col+", "+val+")");
    }

    public boolean equals(Object x){
      boolean eq = false;
      Entry compare;
      if(x instanceof Entry){
        compare = (Entry)x;
        eq = (this.val == compare.val && this.col == compare.col);
      }
      return eq;
    }

  }

  //returns the dimensions of the nxn matrix (n)
  int getSize(){
    return size;
  }

  //returns the number of non zero entries of the matrix
  int getNNZ(){
    return nnz;
  }

  //equals() checks if two matrices are the same
  public boolean equals(Object x){
    boolean eq = false;
    Matrix compare;
    if(x instanceof Matrix){
      compare = (Matrix)x;
      if(this.getSize()!=compare.getSize() || this.getNNZ() != compare.getNNZ()){
        return eq;
      }else{
        for(int i=1;i<size+1;i++){
          List A = this.row[i];
          List B = compare.row[i];
          if(A.length()!=B.length())return false;
          else{
            A.moveFront();
            B.moveFront();
            while(A.index()!=-1){
              Entry one = (Entry)A.get();
              Entry two = (Entry)B.get();
              if(one.col!= two.col || one.val!=two.val){
                return false;
              }
              A.moveNext();
              B.moveNext();
            }
            eq = true;
          }
        }
      }
    }
    return eq;
  }


  //Sets matrix to zero state (empty matrix )
  void makeZero(){
    nnz = 0;
    row = new List[size+1]; //Creates an array of size n+1
    for(int i=1; i< size+1; i++){
      row[i] = new List();
    }
  }

  //Returns a copy of the current matrix
  Matrix copy(){
    Matrix copy = new Matrix(size);
    for(int i=1;i<size+1;i++){ //Iterates through the arrays
      List  A  = row[i];
      List that = copy.row[i];
      A.moveFront();
      if(A.length()==0)continue;
      else{
        while(A.index()!=-1){ //Iterates through the list
          Entry entry = (Entry)A.get();
          Entry toInsert = new Entry(entry.col, entry.val);
          that.append(toInsert);
          copy.nnz++;
          A.moveNext();
        }
      }
    }
    return copy;
  }

  //Changes the entry at the specified row and column to the the double x
  void changeEntry(int i, int j, double x){
    if(i<1 || j<1 || i>getSize() || j>getSize()){
      throw new RuntimeException("changeEntry() error: Attempting to perform an invalid changeEntry");
    }
      Entry entry = new Entry(j, x); // Creates a new entry
      boolean zero = false;
      if(x == 0.0) zero = true;
      if(row[i].length()==0 && zero){ //If the row is empty and value is zero
        return;
      }else if(row[i].length()==0 && !zero){ //If nothing in row and value is 0.0, do nothing
        row[i].append(entry);
        nnz++;
        return;
      }else if(row[i].length()>0 && !zero){ //If nonempty list and nonzero x value
        row[i].moveFront(); //Place the cursor at the front
        Entry cursorEntry =(Entry)row[i].get();
        while(row[i].index()!=-1){
          if(cursorEntry.col == j){
            cursorEntry.val = x;
            return;
          }else if(cursorEntry.col>j){ //If the column is greater than j, you are ahead of the desired position
            row[i].insertBefore(entry); //Insert the entry before the cursor
            nnz++;
            return;
          }else if(cursorEntry == row[i].back()){ //If cursor is at the back, place entry after the tail
            row[i].insertAfter(entry);
            nnz++;
            return;
          }
          row[i].moveNext();
          cursorEntry =(Entry)row[i].get();
        }//End of while
      }else if(row[i].length()>0 && zero){ //If list is not empty and x is 0.0
        row[i].moveFront(); //Place the cursor at the front
        Entry cursorEntry =(Entry)row[i].get();
        while(row[i].index()!=-1){ //Iterate through the list
          if(cursorEntry.col == j){ //If you are arrive at the desired position, delete what is there
            row[i].delete();
            nnz--;
            return;
          }else if(cursorEntry.col>j || cursorEntry == row[i].back())return;
          row[i].moveNext();
          cursorEntry =(Entry)row[i].get();
        }
      }
    }

  //Multiplies a matrix by a scalar
  Matrix scalarMult(double x){
    Matrix mult = new Matrix(size); //empty matrix that will be returned
    for(int i=1;i< size+1;i++){//Loop iterates through each index of the array starting at index 1
      int counter =0;
      row[i].moveFront();
      while(counter<row[i].length()){//Loop iterates through the list
        Entry cursor = (Entry)row[i].get();
        mult.changeEntry(i,cursor.col,x*cursor.val); //Add the new entry but multiply the value by the scalar first
        row[i].moveNext();
        counter++;
      }
    }
    return mult;
  }

  //Transposes a matrix, rows are now columns and vice versa
  Matrix transpose(){
    Matrix result = new Matrix(size);
    for(int i=1; i<size+1; i++){ //loop iterates through the array/rows
      for(row[i].moveFront(); row[i].index()!=-1; row[i].moveNext()){ //Iterate through the list
        Entry cursor = (Entry)row[i].get();
        result.changeEntry(cursor.col,i,cursor.val); //Swap the column and row
      }
    }
    return result;
  }

  //add() returns matrix addition between two matrices
  Matrix add(Matrix M){
    if(getSize()!=M.getSize()){
      throw new RuntimeException("add() error: Attempting to add matrices of different size");
    }
    Matrix one = this.copy();
    Matrix two = M.copy(); //two will be the matrix that is returned
    for(int i=1;i<size+1;i++){ //Iterate through the array
      one.row[i].moveFront(); //Place cursors at the front of the list
      two.row[i].moveFront();
      if(one.row[i].index()==-1) continue;
      else if(one.row[i].index()!=-1 && two.row[i].index()!=-1){ //If neither list is empty
        while(one.row[i].index()!=-1){
          if(two.row[i].index()==-1){
            while(one.row[i].index()!=-1){ //While there are still elements in the list
              Entry cursor = (Entry)one.row[i].get();
              two.row[i].append(cursor);
              one.row[i].moveNext();
              two.nnz++;
            }
            continue;
          }
          Entry cursor1 = (Entry)one.row[i].get();
          Entry cursor2 = (Entry)two.row[i].get();

          if(cursor1.col==cursor2.col){ //If column values are equal, add their values
            cursor2.val += cursor1.val;
            one.row[i].moveNext();
            two.row[i].moveNext();
          }else if(cursor1.col<cursor2.col){ //If the column value is less, add the smaller column value
            two.row[i].insertBefore(cursor1);
            two.nnz++;
            one.row[i].moveNext();
          }else if(cursor1.col>cursor2.col){
            two.row[i].moveNext();
          }
        }
      }
    }
    for(int i=1;i<size+1;i++){ //Iterate through the array
      List current = two.row[i];
      current.moveFront();
      while(current.index()!=-1){ //Iterate through the list
        Entry entry = (Entry)current.get();
        if(entry.val==0.0){ //If the value of the entry is 0.0, delete it
          current.delete();
          two.nnz--;
          current.moveFront();
          continue;
        }
        current.moveNext();
      }
    }
    return two;
  }

  //Subtracts two matrices
  Matrix sub(Matrix M){
    if(getSize()!=M.getSize()){ //If sizes are not equal, throw an exception
      throw new RuntimeException("sub error: sizes are not equal");
    }else{
      Matrix B = M.scalarMult(-1); //Scale the matrix you want to subtract by -1
      Matrix A = this.copy();
      Matrix result = A.add(B); //Add by the negative value of matrix M
      return result;
    }
  }

  //dot returns the dot products between two lists
  private static double dot(List P, List Q){
    double result = 0.0;
    P.moveFront(); //Move front on both lists
    Q.moveFront();
    if(P.index()==-1 || Q.index()==-1) return 0.0; //If length of both lists are zero, return zero
    while(P.index()!=-1){
      if(Q.index()==-1){
        return result;
      }
      Entry entry1 = (Entry)P.get();
      Entry entry2 = (Entry)Q.get();
      if(entry1.col == entry2.col){ //If entries are equal, add the product of the two values to result
        result += (entry1.val*entry2.val);
        Q.moveNext();
        P.moveNext();
      }else if(entry1.col<entry2.col){ //If the col value is less, just move up
        P.moveNext();
      }else{
        Q.moveNext();
      }
    }
    return result;
  }

  //mult perfroms matrix multiplication
  Matrix mult(Matrix M){
    if(this.getSize()!=M.getSize()){
      throw new RuntimeException("mult() error: Attempting to multiply matrices of different size");
    }
    Matrix result = new Matrix(size);
    Matrix a1 = this.copy();
    Matrix a2 = M.copy();
    Matrix transpose = a2.transpose(); //Transposing the matrix to be multiplied makes obtaining the product much easier
    for(int i=1; i<=getSize(); i++){ //Iterate until dimension size
      List l1 = a1.row[i];
      for(int j=1; j<=getSize();j++){ //Iterate until end of the list
        List l2 = transpose.row[j];
        double dotProduct = dot(l1,l2); //Calculate the dot product and change the entry
        result.changeEntry(i,j,dotProduct);
      }
    }


    return result;
  }

  //toString() overrides objects toString
  public String toString(){
    String value = "";
    for(int i = 1; i< size+1; i++){ //iterate through the array
      if(row[i].length()!=0){
        value += i+": "+row[i].toString()+"\n"; //Concatenate to string value
      }
    }
    return value;
  }

}
