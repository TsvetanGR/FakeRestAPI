package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorsModel {
        public int id;
        public int idBook;
        public String firstName;
        public String lastName;

    public AuthorsModel() {}

        public AuthorsModel(int id, int idBook, String firstName, String lastName) {
            this.id = id;
            this.idBook = idBook;
            this.firstName = firstName;
            this.lastName = lastName;
        }

}
