function Amplitude() {
}

Amplitude.prototype.initialize = function(apiKey, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Amplitude", "initialize", [apiKey]);
};

module.exports = new Amplitude();
