const STATE_KEY = "state";

export const loadState = () => {
    try {
        const serializedState = localStorage.getItem(STATE_KEY);
        if (serializedState === null) {
            return undefined;
        }
        return JSON.parse(serializedState);
    } catch (err) {
        return undefined;
    }
};

export const saveState = state => {
    try {
        const serializedState = JSON.stringify(state);
        localStorage.setItem(STATE_KEY, serializedState);
    } catch {
        // ignore write errors
    }
};

export const cleanState = () => {
    try {
        localStorage.removeItem(STATE_KEY);
    } catch {
        // ignore write errors
    }
};

export const getAccessToken = () => {
    try {
        const serializedState = localStorage.getItem(STATE_KEY);
        if (serializedState === null) {
            return undefined;
        }
        const state = JSON.parse(serializedState);
        return (state.user || {}).access_token;
    } catch (err) {
        return undefined;
    }
};

export const isAuthenticated = () => {
    const token = getAccessToken();
    return token !== undefined && token !== "";
};
