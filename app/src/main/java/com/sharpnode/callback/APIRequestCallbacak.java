package com.sharpnode.callback;

/**
 * call back handler for handling webservice responses
 */
public interface APIRequestCallbacak {

    /**
     * Called, When ajax response in successfully transformed into the desired object
     * @param name   string call name returned from ajax response on success
     * @param object object returned from ajax response on success
     */
    void onSuccess(String name, Object object);

    /**
     * Called, When there happens any kind of error, exception or failure in getting ajax response from the server
     *
     * @param name   string call name returned from ajax response on failure
     * @param object returned from ajax response on failure
     */
    void onFailure(String name, Object object);

}
