//Nevan Samadhana
//pa3 List ADT
//Student ID:1539153

class List{
  //Fields for list
  private Node head;
  private Node tail;
  private Node front;
  private Node back;
  private Node cursor;
  private int length;
  private int cursorIndex;

  public class Node{
    //fields for node class
    Object data;
    Node previous;
    Node next;

    //Constructor for Node
    private Node(Object data){
      this.data = data;
      this.next = null;
      this.previous = null;
    }

    public String toString(){
      return String.valueOf(data);
    }

    public boolean equals(Object x){
      boolean eq = false;
      if(x instanceof Node){
        Node compare = (Node)x;
        eq = this.data==compare.data;
      }
      return eq;
    }
  }

  //Constructor for list
  public List(){
    this.head = null;
    this.tail = null;
    this.front = null;
    this.back = null;
    this.cursor = null;
    this.length = 0;
    this.cursorIndex = -1;
  }

  void display(List doubly){ //(WORKING)
    Node current = doubly.head;
    System.out.println("START");
    while(current!=null){
      System.out.println(current.data);
      current = current.next;
    }
    System.out.println("END");
  }

  // places the cursor at the head of a non empty list
  void moveFront(){
    if(length>0){
      cursor = head;
      cursorIndex=0;
    }else{
      return;
    }
  }

  //moveNext moves the cursor one towards the back of the list
  void moveNext(){
    if(cursor!=null && cursor!= tail){
      cursor = cursor.next;
      cursorIndex+=1;
    }else if(cursor==tail){
      cursor = null;
      cursorIndex=-1;
    }else if(cursor ==null){
      return;
    }

  }

  //moveBack places the cursor at the tail of a non empty lists
  void moveBack(){
    if(length>0){
      cursor = tail;
      cursorIndex=length-1;
    }else{
      return;
    }
  }

  //movePrevious moves the cursor one towards the front of the list
  void movePrev(){
    if(cursor!=null && cursor!= head){
      cursor = cursor.previous;
      cursorIndex-=1;
    }else if(cursor==head){
      cursor = null;
      cursorIndex=-1;
    }else if(cursor ==null){
      return;
    }

  }

  //insertAfter places a ndoe after the cursorIndex(WORKING)
  void insertAfter(Object data){
    Node latest = new Node(data);
    if(length==0 || cursorIndex <0){
      throw new RuntimeException(
        "List Error: insertAfter() called either on empty List or list with undefined cursor");
    }else if(cursor == tail){ //If the cursor is the tail
      tail.next = latest;
      latest.previous = tail;
      tail = latest;
      length++;
    }else if(cursor != tail){
      cursor.next.previous = latest;
      latest.next = cursor.next;
      cursor.next = latest;
      latest.previous = cursor;
      length++;
    }
  }


  //insertBefore places a node before the cursor(WORKING)
  void insertBefore(Object data){
    Node latest = new Node(data);
    if(length==0 || cursorIndex <0){
      throw new RuntimeException(
        "List Error: insertBefore() called either on empty List or list with undefined cursor");
    }else if(cursor == head){ //If cursor is the head
      head.previous = latest; //Make head = latest and set pointers
      latest.next = head;
      latest.next = head;
      head = latest;
      length++;
      cursorIndex++;
    }else if(cursorIndex>0){
      cursor.previous.next = latest;
      latest.previous = cursor.previous;
      latest.next = cursor;
      cursor.previous = latest;
      length++;
      cursorIndex++;
    }
  }

  //equals returns true if both lists are sequentially equal, else returns false(WORKING)
  public boolean equals(Object x){
    boolean eq = false;
    List list;
    if(x instanceof List){
      list = (List) x;
      if(this.length != list.length){
        return eq;
      }else if (this.length==0 && list.length==0){
        eq=true;
      }else{
        Node current1 = head;
        Node current2 = list.head;
        while(current1.next != null){
          if(current1.data != current2.data){
            return false;
          }
          current1 = current1.next;
          current2 = current2.next;
        }
        return true;
      }
    }
    return eq;
  }

  //prepend inserts a node into the front of the list (WORKING)
  void prepend(Object data){
    Node latest = new Node(data);
    if(head==null){
      head = latest;
      tail = latest;
      length++;
    }else if(cursorIndex>=0){//If there is a defined cursor, increment it
      latest.next = head;
      head.previous = latest;
      head = latest;
      length ++;
      cursorIndex++;
    }else{
      latest.next = head;
      head.previous = latest;
      head = latest;
      length ++;
    }
  }

  //append places a node at the end of the list (WORKING)
  void append(Object data){
    Node latest = new Node(data);
    if(head==null){ //If there is nothing in the list, add the latest node and make it the head and tail
      head = latest;
      tail = latest;
      length++;
    }else{
      tail.next = latest;
      latest.previous = tail;
      tail = latest; //Sets the tail at the new end
      length++;
    }
  }

  //length returns the amount of elements in the list(WORKING)
  int length(){
    return length;
  }

  //get returns the cursor element
  Object get(){
    if(length>0 && cursorIndex>=0){
      return cursor.data;
    }else{
      throw new RuntimeException(
        "List Error: get() called either on empty List or list with undefined cursor");
    }
  }

   //index returns the index of the cursor, if undefined, returns -1
   int index(){
     if(cursor==null){
       return -1;
     }else if (cursorIndex<0){
       cursorIndex = -1;
     }
     return cursorIndex;
   }

   //deleteFront removes the first node of the list pre:length>0(WORKING)
   void deleteFront(){ //Check for case if cursor is at the head
     if(length>0 && head != cursor){
       head = head.next;
       head.previous = null;
       length--;
       cursorIndex --;
     }else if(length>0 && head == cursor){ //If lgnth greater than zero and the cursor is the head
       cursor = null;
       cursorIndex = -1;
       length --;
       head = head.next;
       head.previous = null;
     }else{
       throw new RuntimeException(
         "List Error: deleteFront() called on empty List");
     }
   }

   //deleteBack removes the last node of the list
   void deleteBack(){ //Check for case if cursor is at tail(WORKING)
     if(length==1){ //If there is only one element in the list, just clear it
       clear();
     }else if(length>0 && tail != cursor){//If the tail is not the cursor
       tail = tail.previous;
       tail.next = null;
       length--;
     }else if(length>0 && tail==cursor){ //If cursor is at the tail but not th only element
       cursor = null;
       cursorIndex = -1;
       length--;
       tail = tail.previous;
       tail.next = null;
     }else{
       throw new RuntimeException(
         "List Error: deleteBack() called on empty List");
     }
   }

  //Returns Front of the list(WORKING)
  Object front(){
    if(length>0){
      return head.data;
    }else{
      throw new RuntimeException(
        "List Error: Front() called on empty List");
    }
  }

  //Returns the back of the list(WORKING)
  Object back(){
    if(length>0){
      return tail.data;
    }else{
      throw new RuntimeException(
        "List Error: front() called on empty List");
    }
  }

  //() essentially clears the entire list;
  void clear(){
    head = null;
    tail = null;
    cursor = null;
    cursorIndex = -1;
    length = 0;
  }

  //Deletes the cursor
  void delete(){
    if(length==0 && cursorIndex<0){
      throw new RuntimeException(
        "List Error: delete() called on empty list or undefined cursor");
    }else if(head==tail){ //If the head is the tail, reset everything
      head = null;
      tail = null;
      cursor = null;
      length = 0;
      cursorIndex = -1;
    }else if(cursor == head){ //If the cursor is at the head
      head = head.next;
      head.previous = null;
      cursor = null;
      length--;
      cursorIndex = -1;
    }
    else if(cursor==tail){ //If the cursor is at the tail
      tail = tail.previous;
      tail.next = null;
      cursor = null;
      cursorIndex = -1;
      length--;
    }else{
      cursor.previous.next = cursor.next;
      cursor.next.previous = cursor.previous;
      cursor = null;
      cursorIndex = -1;
      length--;
    }
  }

  //Overries Objects toString function
  public String toString(){
    Node current = head;
    String value = "";
    while(current!= null){
      value += String.valueOf(current.data)+ " ";
      current=current.next;
    }
    return value;
  }

}
