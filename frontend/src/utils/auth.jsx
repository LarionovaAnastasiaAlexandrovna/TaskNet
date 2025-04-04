export const getAuthToken = () => {
    return localStorage.getItem("token");
};

export const fetchWithAuth = async (url, options = {}) => {
    const token = getAuthToken();
    if (!token) throw new Error("Нет токена");

    return fetch(url, {
        ...options,
        headers: {
            ...options.headers,
            "Authorization": `Bearer ${token}`,
        },
    });
};