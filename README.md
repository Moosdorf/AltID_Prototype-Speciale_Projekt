# AltID Prototype

A UI prototype of the AltID application, developed as part of my master's thesis at Roskilde University (RUC) 2026. AltID is Denmark's planned eIDAS2-compliant digital identity wallet, developed by Digitaliseringsstyrelsen.

## Disclaimer

This prototype and the associated usability test are developed by Andreas Moosdorf, master's student at RUC 2026. Digitaliseringsstyrelsen is in no way affiliated with this project. The prototype is a UI shell of the application based on publicly available material published by Digitaliseringsstyrelsen. Some design decisions have been made independently where functionality was not specified in the source material. This prototype was developed before the official AltID application was released and the material it is based on is draft material.

## What this is

A functional UI prototype of the AltID wallet application built for usability testing purposes. The prototype simulates the core user flows of the AltID app to allow participants to interact with it as they would with the real application.

The prototype is **horizontal** in scope which means that it covers a wide range of user flows but does not implement the underlying technologies. Everything is simulated. There is no real cryptography, no actual credential issuance, and no connection to real identity providers. MitID authorization is simulated. PID issuance is simulated.

## Covered flows

- Account creation and MitID binding 
- PID issuance — Legitimationsbevis 
- Passport photo import via NFC scan 
- Credential presentation via QR code
- Selective disclosure of attributes
- Age proofissuance and presentation
- Verifier request scenario

## What is not included

- Real cryptographic operations
- Actual WSCD/WSCA integration
- Real NFC chip communication
- Live PID provider or attestation provider endpoints
- Genuine MitID authorization
- ARF-compliant credential formats (SD-JWT, mdoc)

## Built with

- Kotlin
- Jetpack Compose
- Android

## Purpose

This prototype was built exclusively for academic usability testing as part of a master's thesis investigating whether the AltID application meets usable security standards as defined by established frameworks including Yee (2004) and Whitten & Tygar (1999), evaluated against ISO 9241-11 usability criteria.

It is not intended for production use or as a reference implementation of the eIDAS2 ARF.

## License

This project is for academic use only. Not affiliated with or endorsed by Digitaliseringsstyrelsen.
