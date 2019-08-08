// BookController.aidl
package com.jack.jack_aidl;
import com.jack.jack_aidl.Book;

// Declare any non-default types here with import statements

interface BookController {
     List<Book> getBookList();
     void addBookInOut(inout Book book);
}
