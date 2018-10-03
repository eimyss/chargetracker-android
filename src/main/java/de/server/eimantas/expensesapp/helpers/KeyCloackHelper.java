package de.server.eimantas.expensesapp.helpers;

import android.app.Activity;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.jboss.aerogear.android.authorization.oauth2.OAuthWebViewDialog;
import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.pipe.PipeManager;
import org.jboss.aerogear.android.pipe.rest.RestfulPipeConfiguration;
import org.jboss.aerogear.android.pipe.rest.multipart.MultipartRequestBuilder;

import java.io.File;
import java.net.URL;

import de.server.eimantas.expensesapp.entities.Expense;

public class KeyCloackHelper {

    private static final String SHOOT_SERVER_URL = "http://192.168.123.157:8180";
    private static final String AUTHZ_URL = SHOOT_SERVER_URL + "/auth";
    private static final String AUTHZ_ENDPOINT = "/realms/expenses/protocol/openid-connect/auth";
    private static final String ACCESS_TOKEN_ENDPOINT = "/realms/expenses/protocol/openid-connect/token";
    private static final String REFRESH_TOKEN_ENDPOINT = "/realms/expenses/protocol/openid-connect/token";
    private static final String AUTHZ_ACCOOUNT_ID = "test";
    private static final String AUTHZ_CLIENT_ID = "expenses-app";
    private static final String AUTHZ_REDIRECT_URL = "http://oauth2callback";
    private static final String MODULE_NAME = "KeyCloakAuthz";

    static {
        try {
            AuthorizationManager.config(MODULE_NAME, OAuth2AuthorizationConfiguration.class)
                    .setBaseURL(new URL(AUTHZ_URL))
                    .setAuthzEndpoint(AUTHZ_ENDPOINT)
                    .setAccessTokenEndpoint(ACCESS_TOKEN_ENDPOINT)
                    .setRefreshEndpoint(REFRESH_TOKEN_ENDPOINT)
                    .setAccountId(AUTHZ_ACCOOUNT_ID)
                    .setClientId(AUTHZ_CLIENT_ID)
                    .setRedirectURL(AUTHZ_REDIRECT_URL)
                    .asModule();

            PipeManager.config("kc-upload", RestfulPipeConfiguration.class).module(AuthorizationManager.getModule(MODULE_NAME))
                    .withUrl(new URL(SHOOT_SERVER_URL + "/shoot/rest/photos"))
                    .requestBuilder(new MultipartRequestBuilder())
                    .forClass(Expense.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void connect(final Activity activity, final Callback callback) {
        try {
            final AuthzModule authzModule = AuthorizationManager.getModule(MODULE_NAME);

            authzModule.requestAccess(activity, new Callback<String>() {
                @SuppressWarnings("unchecked")
                @Override
                public void onSuccess(String s) {
                    callback.onSuccess(s);
                }

                @Override
                public void onFailure(Exception e) {
                    if (!e.getMessage().matches(OAuthWebViewDialog.OAuthReceiver.DISMISS_ERROR)) {
                        authzModule.deleteAccount();
                    }
                    callback.onFailure(e);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void upload(final File file, final Callback callback, Activity activity) {
        PipeManager.getPipe("kc-upload", activity).save(new Expense(), callback);
    }

    public static boolean isConnected() {
        return AuthorizationManager.getModule(MODULE_NAME).isAuthorized();
    }

}