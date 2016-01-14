package vn.kms.ngaythobet.domain.tournament;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vn.kms.ngaythobet.domain.util.DataInvalidException;

public class ParseData {
    public static List<Tournament> parseTournamentFromLiveScore() {
        List<Tournament> tournaments = new ArrayList<>();
        String baseUrl = "http://www.livescore.com";
        try {
            Document doc = Jsoup.connect(baseUrl).timeout(72000000).get();
            Elements tournamentElements = doc.select("body > div.wrapper > div.left-bar > ul:nth-child(2) > li > a");
            for (Element tournamentElement : tournamentElements) {
                Set<String> competitorInTournament = new HashSet<>();
                List<Round> rounds = new ArrayList<>();
                Tournament tournament = new Tournament();
                tournament.setName(tournamentElement.text());
                String tournamentUrl = baseUrl + tournamentElement.attr("href");
                Document tournamentDoc = Jsoup.connect(tournamentUrl).get();
                Elements roundElements = tournamentDoc
                        .select("body > div.wrapper > div.left-bar > ul:nth-child(5) > li > a");
                if (roundElements.isEmpty()) {
                    roundElements = tournamentDoc
                            .select("body > div.wrapper > div.left-bar > ul:nth-child(2) > li > ul > li > a");
                }
                for (Element roundElenment : roundElements) {
                    Set<String> competitorInRound = new HashSet<>();
                    List<Match> matches = new ArrayList<>();
                    Round round = new Round();
                    round.setName(roundElenment.text());
                    String roundUrl = baseUrl + roundElenment.attr("href");
                    Document roundDoc = Jsoup.connect(roundUrl).timeout(72000000).get();
                    Elements matchElements = roundDoc
                            .select("body > div.wrapper > div.content > div > div.col-7 > div > div.col-10 > a");
                    if (matchElements.isEmpty()) {
                        Elements matchTimeElement = tournamentDoc.select(
                                "body > div.wrapper > div.content > div.row.row-tall.mt4 >div.clear > div.right");
                        Elements leftCompetitorElements = roundDoc
                                .select("body > div.wrapper > div.content > div.row-gray > div.ply.tright.name");
                        Elements rightCompetitorElements = roundDoc
                                .select("body > div.wrapper > div.content > div.row-gray > div.ply.name:not(.tright)");
                        Elements timeElements = roundDoc
                                .select("body > div.wrapper > div.content > div.row-gray > div.min");
                        Elements scoreElements = roundDoc
                                .select("body > div.wrapper > div.content > div.row-gray > div.sco");
                        List<Competitor> leftCompetitors = new ArrayList<>();
                        List<Competitor> rightCompetitors = new ArrayList<>();
                        List<String> times = new ArrayList<>();
                        String mathDateString = matchTimeElement.get(0).text();
                        for (Element leftCompetitorElement : leftCompetitorElements) {
                            Competitor competitor = new Competitor();
                            competitor.setName(leftCompetitorElement.text());
                            leftCompetitors.add(competitor);
                            competitorInRound.add(leftCompetitorElement.text());
                            competitorInTournament.add(leftCompetitorElement.text());
                        }
                        for (Element rightCompetitorElement : rightCompetitorElements) {
                            Competitor competitor = new Competitor();
                            competitor.setName(rightCompetitorElement.text());
                            rightCompetitors.add(competitor);
                            competitorInRound.add(rightCompetitorElement.text());
                            competitorInTournament.add(rightCompetitorElement.text());
                        }
                        for (Element timeElement : timeElements) {
                            if ("FT".equals(timeElement.text()) || "AET".equals(timeElement.text())
                                    || "Canc.".equals(timeElement.text()) || timeElement.text().length() != 5) {
                                times.add("00:00");
                            } else {
                                times.add(timeElement.text());
                            }
                        }
                        for (int i = 0; i < leftCompetitors.size(); i++) {
                            Match match = new Match();
                            match.setCompetitor1(leftCompetitors.get(i));
                            match.setCompetitor2(rightCompetitors.get(i));
                            String matchTimeString = getDateFromString(mathDateString, times.get(i));
                            match.setMatchTime(parseStringToDate(matchTimeString));
                            String[] scores = parseStringToArrayScore(scoreElements.get(i).text());
                            if (!(scores[0].equals("?") || scores[1].equals("?"))) {
                                match.setScore1(Long.parseLong(scores[0]));
                                match.setScore2(Long.parseLong(scores[1]));
                            }
                            matches.add(match);
                        }
                    } else {
                        Elements timeElements = roundDoc
                                .select("body > div.wrapper > div.content > div > div.col-7 > div > div.col-2");
                        Elements dayElements = roundDoc
                                .select("body > div.wrapper > div.content > div.row-gray > div.col-2:nth-child(2)");
                        Elements scoreElements = roundDoc
                                .select("body > div.wrapper > div.content > div.row-gray > div.col-1");
                        List<String> times = new ArrayList<>();
                        List<String> days = new ArrayList<>();
                        for (Element timeElement : timeElements) {
                            if ("FT".equals(timeElement.text()) || "AET".equals(timeElement.text())
                                    || "Canc.".equals(timeElement.text()) || timeElement.text().length() != 5) {
                                times.add("00:00");
                            } else {
                                times.add(timeElement.text());
                            }
                        }
                        for (Element dayElement : dayElements) {
                            days.add(dayElement.text());
                        }
                        for (int i = 0; i < matchElements.size(); i++) {
                            Element matchElement = matchElements.get(i);
                            Match match = new Match();
                            Competitor competitor1 = new Competitor();
                            Competitor competitor2 = new Competitor();
                            String[] competitorNames = matchElement.text().split(" vs ");
                            competitor1.setName(competitorNames[0]);
                            competitor2.setName(competitorNames[1]);
                            match.setCompetitor1(competitor1);
                            match.setCompetitor2(competitor2);
                            String matchTimeString = getDateFromString(days.get(i), times.get(i));
                            match.setMatchTime(parseStringToDate(matchTimeString));
                            String[] scores = parseStringToArrayScore(scoreElements.get(i).text());
                            if (!(scores[0].equals("?") || scores[1].equals("?"))) {
                                match.setScore1(Long.parseLong(scores[0]));
                                match.setScore2(Long.parseLong(scores[1]));
                            }
                            matches.add(match);
                            competitorInRound.add(competitorNames[0]);
                            competitorInRound.add(competitorNames[1]);
                            competitorInTournament.add(competitorNames[0]);
                            competitorInTournament.add(competitorNames[1]);
                        }
                    }
                    round.setMatches(matches);
                    round.setCompetitors(createCompetitors(competitorInRound));
                    rounds.add(round);
                }
                tournament.setRounds(rounds);
                List<Competitor> competitors = new ArrayList<>();
                tournament.setCompetitors(createCompetitors(competitorInTournament));
                tournament.setNumOfCompetitors((long) competitors.size());
                tournaments.add(tournament);
            }
        } catch (IOException e) {
            throw new DataInvalidException("exception.parse.data");
        }
        return tournaments;
    }

    private static List<Competitor> createCompetitors(Set<String> competitorNames) {
        List<Competitor> competitors = new ArrayList<>();
        for (String competitorName : competitorNames) {
            Competitor competitor = new Competitor();
            competitor.setName(competitorName);
            competitors.add(competitor);
        }
        return competitors;
    }

    private static String getDateFromString(String dateString, String timeString) {
        String year = "";
        String[] date = dateString.split(", ");
        if (date.length == 1) {
            year = String.valueOf(LocalDateTime.now().getYear());
        } else {
            year = date[1];
        }
        String[] dayMonth = date[0].split(" ");
        String day = dayMonth[1];
        if (day.length() == 1) {
            day = "0" + day;
        }
        String month = dayMonth[0];
        if (month.length() > 3) {
            month = month.substring(0, 3);
        }

        return year + "-" + month + "-" + day + " " + timeString;
    }

    private static LocalDateTime parseStringToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm");
        return LocalDateTime.parse(dateString, formatter);
    }

    private static String[] parseStringToArrayScore(String scoreString) {
        return scoreString.split(" - ");
    }
}