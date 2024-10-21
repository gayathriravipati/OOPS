import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

// Given a directory - search for the folder in the given directory based on the filters of name / size/ extension
// Also if given multiple filters => Implementation of andFilter


public class FindFolder {
    public static void main(String[] args) throws IOException {
            // Example usage
            String directory = "/Users/gayathri/Downloads/OOPS";
            // Filter filter = new NameFilter("PizzaOrder.java");

            // Programming by interface and not by implementation.
            Filter searchbyName = new NameFilter("PizzaOrder.java");
            Filter searchbyExt = new ExtFilter("java");

            Filter andFilter = new AndFilter(searchbyName, searchbyExt);

            System.out.println(searchFiles(directory, andFilter));


            List<File> results = searchFiles(directory, andFilter);
            results.forEach(file -> System.out.println(file.getAbsolutePath()));
         
    }

    // This method gets the directory to search and based on what filter it has to search
    public static List<File> searchFiles(String directory, Filter filter) throws IOException {
        List<File> matchedFiles = new LinkedList<>();
        List<File> filesToSearch = getAllFilesinDir(directory);
        for (File file : filesToSearch) {
            if (filter.match(file)) {
                matchedFiles.add(file);
            }
        }
        return matchedFiles;
    }

    public static List<File> getAllFilesinDir(String directory) throws IOException {
        List<File> allFiles = new LinkedList<>();
        try (Stream<Path> pathStream = Files.walk(Paths.get(directory), Integer.MAX_VALUE)) {
            pathStream.filter(Files::isRegularFile).forEach(path -> {
                allFiles.add(path.toFile());
            });
        }
        return allFiles;
    }
}

interface Filter {
    // Interface because any class which has to find by filter needs an input f
    // It implements this Interface
    boolean match(File f);
}

class NameFilter implements Filter {
    private final String fileName;

    public NameFilter(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean match(File f) {
        return f.getName().equals(fileName);
    }
}

class ExtFilter implements Filter {
    private final String extname;

    public ExtFilter(String extname) {
        this.extname = extname;
    }

    @Override
    public boolean match(File f) {
        String fileName = f.getName();
        String[] parts = fileName.split("\\.");
        return parts.length > 1 && extname.equals(parts[parts.length - 1]);
    }
}


class AndFilter implements Filter{
	List<Filter> filters;

	public AndFilter(Filter ... filters){
		this.filters = Arrays.asList(filters);
	}

	@Override
	public boolean match(File f) {

	   for(Filter filter : filters){
		   	if(!filter.match(f)){
	        	return false;
	        }
	   }
        return true;
    }
}


/*
Learnings:

1. Using the Interface Type (Filter filter = new NameFilter("example.txt");):

This approach is more flexible because filter can hold any object that implements the Filter interface, not just NameFilter.
It's useful when you want to write more general and reusable code. For example, you can easily switch to a different filter implementation without changing the rest of your code.
This promotes loose coupling and adheres to the principle of programming to an interface, not an implementation.

// Using a name filter
Filter filter = new NameFilter("example.txt");
List<File> results = searchFiles(directory, filter);

// Now using a different filter, e.g., SizeFilter
filter = new SizeFilter(1024); // Example: filters files larger than 1024 bytes
results = searchFiles(directory, filter);


// Using a name filter
NameFilter nameFilter = new NameFilter("example.txt");
List<File> results = searchFiles(directory, nameFilter);

// Now using a different filter, e.g., SizeFilter
SizeFilter sizeFilter = new SizeFilter(1024); // Example: filters files larger than 1024 bytes
results = searchFiles(directory, sizeFilter);


2. Rules with throws keyword

Rule 1: Declare Exceptions in Method Signatures
If a method performs an operation that might throw an exception, such as reading from or writing to a file, the method must declare this potential exception in its signature using the throws keyword.
Rule 2: Propagate Exceptions Appropriately
When a method calls another method that throws a checked exception, the calling method must either handle the exception using a try-catch block or declare that it throws the exception as well.
Rule 3: Interface and Implementation Declaration
If a method in an interface is expected to throw an exception, the interface method must declare the exception. Any class implementing this interface must also declare the exception in the corresponding method.

3. Dont miss the annotation @Override when overriding the methods which are first initially giben by interface.

*/
