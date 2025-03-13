package com.swdprojects.romkaicode.dmsgui;

/*
    Roberto Ruiz,CEN3024C, 3/12/2025
    Software Development I
    FilmController: this class controls the main functionality and validation of the films handled the entire application
* */


import java.io.*;
import java.util.*;

public class FilmController
{
    private List<Film> films;
    private Scanner scanner;

    public FilmController()
    {
        films = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    /*
     * GetMenuText: shows a main menu to the user
     * args: none
     * return: String
     * */
    public String GetMenuText()
    {
        return """
                \nFilm Management System:
                1. Add Film Manually
                2. Load Films from File
                3. Remove Film
                4. Update Film
                5. Display Films
                6. Aggregate Runtimes
                7. Exit
                Choose an option:
                """;
    }



    /*
     * menuDisplay: takes user option from switch to take and validate input
     * args: none
     * return: none
     * */
    public boolean MenuOptionValidator()
    {
        while (true) {
            //catch number/string errors here
            try {
                System.out.print(GetMenuText());

                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.print("Enter Film ID (4-digit number): ");
                        int filmId = Integer.parseInt(scanner.nextLine());
                        if (!validateID(filmId) || duplicateFilmCheck(filmId)) {
                            System.out.println("Invalid or duplicate Film ID. It must be a unique 4-digit number.");
                            break;
                        }
                        System.out.print("Enter Film Name: ");
                        String filmName = scanner.nextLine();
                        if (!validateString(filmName)) {
                            System.out.println("Invalid Film Name.");
                            break;
                        }
                        System.out.print("Enter Film Description: ");
                        String filmDescription = scanner.nextLine();
                        if (!validateString(filmDescription)) {
                            System.out.println("Invalid Film Description.");
                            break;
                        }
                        System.out.print("Enter Film Price: ");
                        float filmPrice = Float.parseFloat(scanner.nextLine());
                        if (!validatePrice(filmPrice)) {
                            System.out.println("Invalid Film Price. It must be a positive number.");
                            break;
                        }
                        System.out.print("Enter Film Release Date: ");
                        int filmReleaseDate = Integer.parseInt(scanner.nextLine());
                        if (!validatePositiveInt(filmReleaseDate)) {
                            System.out.println("Invalid Film Release Date. It must be a positive integer.");
                            break;
                        }
                        System.out.print("Enter Movie Runtime: ");
                        int movieRuntime = Integer.parseInt(scanner.nextLine());
                        if (!validatePositiveInt(movieRuntime)) {
                            System.out.println("Invalid Movie Runtime. It must be a positive integer.");
                            break;
                        }

                        if (addFilmManually(filmId, filmName, filmDescription, filmPrice, filmReleaseDate, movieRuntime)) {
                            System.out.println("Film added successfully.");
                        } else {
                            System.out.println("Invalid input. Film not added.");
                        }
                        break;
                    case 2:
                        System.out.print("Enter filename (full path): ");
                        String filename = scanner.nextLine();
                        if (loadFilmsFromFile(filename)) {
                            System.out.println("Films loaded successfully.");
                        } else {
                            System.out.println("Error loading films.");
                        }
                        break;
                    case 3:
                        System.out.print("Enter Film ID to remove: ");
                        int removeId = Integer.parseInt(scanner.nextLine());
                        if (removeFilm(removeId)) {
                            System.out.println("Film removed successfully.");
                        } else {
                            System.out.println("Film not found.");
                        }
                        break;
                    case 4:
                        System.out.print("Enter Film ID to update: ");
                        int updateId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter new name (or press Enter to skip): ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter new description (or press Enter to skip): ");
                        String newDescription = scanner.nextLine();
                        System.out.print("Enter new price (or press Enter to skip): ");
                        String priceInput = scanner.nextLine();
                        Float newPrice = priceInput.isEmpty() ? null : Float.parseFloat(priceInput);
                        System.out.print("Enter new release date (or press Enter to skip): ");
                        String dateInput = scanner.nextLine();
                        Integer newReleaseDate = dateInput.isEmpty() ? null : Integer.parseInt(dateInput);
                        System.out.print("Enter new runtime (or press Enter to skip): ");
                        String runtimeInput = scanner.nextLine();
                        Integer newRuntime = runtimeInput.isEmpty() ? null : Integer.parseInt(runtimeInput);

                        if (updateFilm(updateId, newName, newDescription, newPrice, newReleaseDate, newRuntime)) {
                            System.out.println("Film updated successfully.");
                        } else {
                            System.out.println("Film not found or invalid input.");
                        }
                        break;
                    case 5:
                        System.out.println(displayAllFilms());
                        break;
                    case 6:
                        System.out.println("Total Runtime: " + getAllFilmRuntime() + " minutes");
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        return false;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Please enter a valid number.");
            }
            catch (Exception e)
            {
                System.out.println("Error, restarting menu...: " + e.getMessage());
            }
        }
    }

    /*
     * validateID: checks if the ID format is valid
     * args: ID input
     * return: boolean value (validation)
     * */
    private boolean validateID(int ID)
    {
        //checker for 4 digit requirement filmID
        return ID >= 1000 && ID <= 9999;
    }

    /*
     * validateID: checks if the string input format is valid
     * args: string input
     * return: boolean value (validation)
     * */
    private boolean validateString(String input)
    {
        return input != null && !input.trim().isEmpty();
    }

    /*
     * validateID: checks if the float input format is valid
     * args: float
     * return: boolean value (validation)
     * */
    private boolean validatePrice(float price)
    {
        return price > 0;
    }

    /*
     * validateID: checks if the int input format is valid and a positive number
     * args: float
     * return: boolean value (validation)
     * */
    private boolean validatePositiveInt(int value)
    {
        return value > 0;
    }

    /*
     * validateID: checks if the ID input format is already in the film list
     * args: float
     * return: boolean value (validation)
     * */
    private boolean duplicateFilmCheck(int ID)
    {
        return films.stream().anyMatch(film -> film.getID() == ID);
    }

    /*
     *  loadFilmsFromFile: adds a series of film inputs from a text file
     * args: filename string
     * return: boolean value (validation)
     * */
    public boolean loadFilmsFromFile(String filename)
    {
        //set try/catch line reader for each film in the file
        try (BufferedReader fileLineReader = new BufferedReader(new FileReader(filename))) {
            String line;
            List<Film> tempFilms = new ArrayList<>();

            //while EOF not reached, trim inputs line by line and put in a film object
            //skipline helper function will phase through rest of film entries
            while ((line = fileLineReader.readLine()) != null)
            {
                try {
                    int filmId = Integer.parseInt(line.trim());
                    if (!validateID(filmId))
                    {
                        System.out.println("Skipping entry: Invalid Film ID - " + filmId);
                        skipLines(fileLineReader, 5);
                        continue;
                    }

                    String filmName = fileLineReader.readLine().trim();
                    if (!validateString(filmName))
                    {
                        System.out.println("Skipping entry: Invalid Film Name");
                        skipLines(fileLineReader, 4);
                        continue;
                    }

                    String filmDescription = fileLineReader.readLine().trim();
                    if (!validateString(filmDescription))
                    {
                        System.out.println("Skipping entry: Invalid Film Description");
                        skipLines(fileLineReader, 3);
                        continue;
                    }

                    float filmPrice = Float.parseFloat(fileLineReader.readLine().trim());
                    if (!validatePrice(filmPrice))
                    {
                        System.out.println("Skipping entry: Invalid Film Price");
                        skipLines(fileLineReader, 2);
                        continue;
                    }

                    int filmReleaseDate = Integer.parseInt(fileLineReader.readLine().trim());
                    if (!validatePositiveInt(filmReleaseDate))
                    {
                        System.out.println("Skipping entry: Invalid Film Release Date");
                        skipLines(fileLineReader, 1);
                        continue;
                    }

                    int movieRuntime = Integer.parseInt(fileLineReader.readLine().trim());
                    if (!validatePositiveInt(movieRuntime)) {
                        System.out.println("Skipping entry: Invalid Movie Runtime");
                        continue;
                    }

                    tempFilms.add(new Film(filmId, filmName, filmDescription, filmPrice, filmReleaseDate, movieRuntime));

                }
                catch (NumberFormatException | NullPointerException e)
                {

                }
            }

            films.addAll(tempFilms);
            return true;

        }
        catch (IOException e)
        {
            System.out.println("Error reading file: " + e.getMessage());
            return false;
        }
    }

    /*
     *  skipLines: workaround for blank lines for invalid film data, skips to next X amount of lines
     * args: Bufferreader, number of lines to skip
     * return: none
     * */
    private void skipLines(BufferedReader reader, int numLines) throws IOException
    {
        for (int i = 0; i < numLines; i++)
        {
            //if line is empty, move onto next line for input
            if (reader.readLine() == null) break; // Stop if EOF
        }
    }


    /*
     *  removeFilm: removes film via ID from the List of films
     * args: ID
     * return: boolean value  (validation)
     * */
    public boolean removeFilm(int ID)
    {
        return films.removeIf(film -> film.getID() == ID);
    }

    /*
     *  updateFilm: changes existing film object attributes
     * args: ID, new Name, new Description, new Price,  new ReleaseDate,  new Runtime
     * return: boolean value
     * */
    public boolean updateFilm(int ID, String newName, String newDescription, Float newPrice, Integer newReleaseDate, Integer newRuntime) {
        for (Film film : films)
        {
            if (film.getID() == ID) {
                if (newName != null && !newName.isEmpty()) film.setName(newName);
                if (newDescription != null && !newDescription.isEmpty()) film.setDescription(newDescription);
                if (newPrice != null && newPrice >= 0) film.setPrice(newPrice);
                if (newReleaseDate != null && newReleaseDate >= 0) film.setReleaseDate(newReleaseDate);
                if (newRuntime != null && newRuntime > 0) film.setRuntime(newRuntime);
                return true;
            }
        }
        //film not found, exit
        return false;
    }

    /*
     *  addFilmManually: adds film object from manual data entry to the List
     * args: ID,  Name, desciptions ,Price, ReleaseDate, Runtime
     * return: boolean value (validation)
     * */
    public boolean addFilmManually(int ID, String name, String description, float price, int releaseDate, int movieRuntime) {
        if (!validateID(ID) || !validateString(name) || !validateString(description) || !validatePrice(price) || !validatePositiveInt(releaseDate) || !validatePositiveInt(movieRuntime) || duplicateFilmCheck(ID)) {
            return false; // Invalid data
        }
        films.add(new Film(ID, name, description, price, releaseDate, movieRuntime));
        return true;
    }

    /*
     *  getAllFilmRuntime: adds all film runtime and aggregates them into a total number
     * args: none
     * return: integer value (total)
     * */
    public int getAllFilmRuntime()
    {
        if (films.isEmpty())
        {
            return 0;
        }
        int totalRuntime = 0;
        for (Film film : films)
        {
            totalRuntime += film.getRuntime();
        }
        return totalRuntime;
    }


    /*
     *  getAllFilms: returns all film object in a List
     * args: none
     * return: List of film objects
     * */
    public List<Film> getAllFilms()
    {
        return films;
    }

    /*
     *  displayAllFilms: loops through all films to user in the List
     * args: none
     * return: builder instance tostring
     * */
    public String displayAllFilms()
    {
        if (films.isEmpty())
        {

            return "No movies found.\n";
        }
        //mutable string to format all film toStrings together
        StringBuilder builder = new StringBuilder();
        System.out.println("===MOVIES ===\n");
        for (Film film : films) {
            builder.append(film.toString()).append("\n");
        }
        return builder.toString();
    }

    /*
     *  getFilmByID: loops through all films in list to find existing film by ID
     * args: ID(int)
     * return: film object
     * */
    public Film getFilmByID(int ID)
    {
        for (Film film : films)
        {
            if (film.getID() == ID)
            {return film;}
        }
        return null;
    }


}
