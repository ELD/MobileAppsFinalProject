Twovie Times Alpha Test README

Launch the app and select a movie theater. From there, check out the datepicker and the movie selection.

What doesn't work:
    - Network Connection: questionable whether it's implemented in the Alpha. If not, we use mocked data
        for app usage.
    - Movie Selection Buttons: Not implemented yet. Short sightedness on the model layer requires some
        internal refactoring to make it work.
    - Location Services: not yet implemented but will be to aid in picking movie locations. For now, the
        zip code of 80401 (Golden, CO) is hard-coded.
    - Movie Pairings: The core functionality isn't quite implemented. This is contingent on several other
        feature completions. For now, the data is mocked.

Focus of the alpha test:
    For the alpha testing period, focus on the UI features and suggest UI cleanups. The functionality
    will work in the beta release, but for right now, the app is more or less an interactive storyboard.
    We were caught unaware by time pressures and we underestimated the effort required to get some
    functionality working in the alpha release (as I'm sure many groups did).

