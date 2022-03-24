package com.aoservice.exceptions.coreExceptionClasses;



public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing required field"),
    INTERNAL_SERVER_ERROR("Internal PrestaLink server error"),
    NO_APPELOFFRE_FOUND("aucune appel offre trouvé"),
    PROFILE_NOT_CREATED("profile non created");

    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
