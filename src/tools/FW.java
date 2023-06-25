package tools;

import border.PoliceTerminal;
import sample.Simulation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class FW extends Thread{

    private static final String STATUS_FILE_PATH = "status.txt";

    @Override
    public void run () {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path directory = Paths.get(".");
            directory.register(watchService, ENTRY_MODIFY);

                    while (true) {
                        WatchKey key = watchService.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            if (event.context().toString().equals(STATUS_FILE_PATH)) {
                                handleStatusChange();
                            }
                        }
                        key.reset();
                    }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void handleStatusChange() throws IOException, InterruptedException {
        String[] terminalStatuses = readStatus().split("\n");
        for (String terminalStatus : terminalStatuses) {
            String[] parts = terminalStatus.split(" "); //Terminal 1 blocked
            if (parts.length == 3) {
                String terminal = parts[0]; //PoliceTerminal
                int id = Integer.parseInt(parts[1]);  //1
                String status = parts[2]; //blocked
                    // Terminal can proceed with processing
                synchronized (Simulation.terminalsMap) {

                    if (Simulation.terminalsMap.containsKey(id)) {
                        if (Simulation.terminalsMap.get(id).getStatus().equals("released")) {
                            if (status.equals("blocked\r")) {

                                System.out.println("nit je iz stanja rada,presla u stanje blokiranja");
                                Simulation.terminalsMap.get(id).setStatus("blocked");

                                synchronized (Simulation.terminalsMap.get(id)) {
                                    Simulation.terminalsMap.get(id).suspend();
                                }
                            }
                        } else if (Simulation.terminalsMap.get(id).getStatus().equals("blocked")) {
                            if (status.equals("released\r")) {
                                System.out.println("nit je iz stanja blokade,presla u stanje rada");
                                Simulation.terminalsMap.get(id).setStatus("released");
                                if (Simulation.terminalsMap.get(id).getState() == Thread.State.NEW) {
                                    Simulation.terminalsMap.get(id).start();
                                }
                                else {
                                    synchronized (Simulation.terminalsMap.get(id)) {
                                        Simulation.terminalsMap.get(id).resume();
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private static String readStatus() throws IOException {
        return Files.readString(Paths.get(STATUS_FILE_PATH)).trim();
    }

    private static void setStatus(String terminal,int id, String status) throws IOException {
        String newStatus = terminal + " " +id+" "+status;
        String existingStatuses = readStatus();
        String updatedStatuses = existingStatuses.replaceFirst("(?m)^" + terminal + "\\s+\\w+", newStatus);

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(STATUS_FILE_PATH), StandardCharsets.UTF_8)) {
            writer.write(updatedStatuses);
        }

        System.out.println("Status set for " + terminal + ": " + status);
    }
}
