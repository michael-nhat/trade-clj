# Mercurius

[WIP]

[![Build Status](https://travis-ci.org/eeng/mercurius.svg?branch=master)](https://travis-ci.org/eeng/mercurius)

## TODO README

Backend

- Store some trx-id in deposits/withdraws related to transfers
- Simulator, place orders around current price
- Volume in USD
- Spec the domain events, and add asserts to the notifier
- The partial publish-event function doesn't allow to spec the events
- Publish wallet events so we can display a balances panel

Frontend

- Add a change indicator to trades
- Manage deps with deps.edn?
- Unify the events (ns keywords or not?)
