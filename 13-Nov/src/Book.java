

public class Book {
	String title;
	String authName;
	int price;
	public Book(String title,String name,int price) {
		this.title=title;
		this.authName=name;
		this.price=price;
	}
	public void displayBook() {
		System.out.println("Book Name: "+title);
		System.out.println("Author Name: "+authName);
		System.out.println("Book Price: "+price);
	}
	public static void main(String[] args) {
		Book b1=new Book("A Thousand Splendid Suns","Khaled Hosseini",400);
		b1.displayBook();
	}
}
