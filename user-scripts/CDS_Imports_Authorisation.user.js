// ==UserScript==
// @name        CDS Imports Authorisation
// @namespace   http://tampermonkey.net/
// @version     0.1
// @description Authenticates with CDS Declare
// @author      You
// @match       http*://*/auth-login-stub/gg-sign-in*
// @grant       none
// @updateURL   to be added after merging to master for correct URL
// ==/UserScript==

(function() {
    'use strict';

    prePopulateFields();

    document.getElementById('global-header').appendChild(createQuickButton());
})();

function prePopulateFields() {
    document.getElementsByName("redirectionUrl")[0].value = getBaseUrl() + "/customs/imports/";

    document.getElementById("affinityGroupSelect").selectedIndex = 1;

    document.getElementsByName("enrolment[0].name")[0].value = "HMRC-CUS-ORG";
    document.getElementById("input-0-0-name").value = "EORINumber";
    document.getElementById("input-0-0-value").value = isExternalTestEnv() ? "GB1234567890" : "GB71727550" + (Math.floor(Math.random()*90000) + 10000);

    document.getElementsByName("itmp.dateOfBirth")[0].value = "1993-06-30";
}

function isExternalTestEnv() {
    return window.location.host === 'test-www.tax.service.gov.uk';
}

function createQuickButton() {
    const button = document.createElement('button');
    button.id="quickSubmit";
    button.innerHTML = 'Quick Submit';
    button.onclick = () => document.getElementsByClassName('button')[0].click();
    button.setAttribute('style', 'position: fixed; top: 10px; left: 20px;');
    return button;
}

function getBaseUrl() {
    let host = window.location.host;
    if (window.location.hostname === 'localhost') {
        host = 'localhost:9760'
    }
    return window.location.protocol + "//" + host;
}