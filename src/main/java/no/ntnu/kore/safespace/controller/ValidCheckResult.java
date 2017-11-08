package no.ntnu.kore.safespace.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidCheckResult {

    public static ValidCheckResult OK = new ValidCheckResult(true, "Valid");

    private boolean valid;
    private String message;

}
