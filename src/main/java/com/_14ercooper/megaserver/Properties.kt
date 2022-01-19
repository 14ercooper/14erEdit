// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.megaserver

object Properties {
    var server = """
           spawn-protection=0
           max-tick-time=60000
           query.port=25565
           generator-settings=
           force-gamemode=false
           allow-nether=true
           enforce-whitelist=false
           gamemode=survival
           broadcast-console-to-ops=true
           enable-query=false
           player-idle-timeout=0
           difficulty=peaceful
           spawn-monsters=true
           broadcast-rcon-to-ops=true
           op-permission-level=4
           pvp=true
           snooper-enabled=true
           level-type=default
           hardcore=false
           enable-command-block=true
           max-players=20
           network-compression-threshold=256
           resource-pack-sha1=
           max-world-size=29999984
           function-permission-level=2
           rcon.port=25575
           server-port=25565
           debug=false
           server-ip=
           spawn-npcs=true
           allow-flight=false
           view-distance=10
           resource-pack=
           spawn-animals=true
           white-list=true
           rcon.password=
           generate-structures=true
           online-mode=true
           max-build-height=256
           level-seed=
           use-native-transport=true
           prevent-proxy-connections=false
           motd=The Mapmaking Megaserver
           enable-rcon=false
           
           """.trimIndent()
    var spigot = """# This is the main configuration file for Spigot.
# As you can see, there's tons to configure. Some options may impact gameplay, so use
# with caution, and make sure you know what each option does before configuring.
# For a reference for any variable inside this file, check out the Spigot wiki at
# https://www.spigotmc.org/wiki/spigot-configuration/
# 
# If you need help with the configuration or have any questions related to Spigot,
# join us at the IRC or drop by our forums and leave a post.
# 
# IRC: #spigot @ irc.spi.gt ( https://www.spigotmc.org/pages/irc/ )
# Forums: https://www.spigotmc.org/

config-version: 12
settings:
  debug: false
  save-user-cache-on-stop-only: false
  bungeecord: false
  user-cache-size: 1000
  moved-wrongly-threshold: 0.0625
  moved-too-quickly-multiplier: 10.0
  log-villager-deaths: true
  timeout-time: 60
  restart-on-crash: true
  restart-script: ./start.sh
  netty-threads: 4
  sample-count: 12
  player-shuffle: 0
  attribute:
    maxHealth:
      max: 2048.0
    movementSpeed:
      max: 2048.0
    attackDamage:
      max: 2048.0
messages:
  whitelist: Please type "op <username>" into the console
  unknown-command: Unknown command. Type "/help" for help.
  server-full: The server is full!
  outdated-client: Outdated client! Please use {0}
  outdated-server: Outdated server! I'm still on {0}
  restart: Server is restarting
commands:
  replace-commands:
  - setblock
  - summon
  - testforblock
  - tellraw
  log: true
  tab-complete: 0
  send-namespaced: true
  spam-exclusions:
  - /skill
  silent-commandblock-console: false
advancements:
  disable-saving: false
  disabled:
  - minecraft:story/disabled
stats:
  disable-saving: false
  forced-stats: {}
world-settings:
  default:
    max-entity-collisions: 2
    verbose: true
    mob-spawn-range: 6
    hopper-amount: 1
    dragon-death-sound-radius: 0
    seed-village: 10387312
    seed-desert: 14357617
    seed-igloo: 14357618
    seed-jungle: 14357619
    seed-swamp: 14357620
    seed-monument: 10387313
    seed-shipwreck: 165745295
    seed-ocean: 14357621
    seed-outpost: 165745296
    seed-slime: 987234911
    max-tnt-per-tick: 20
    view-distance: default
    enable-zombie-pigmen-portal-spawns: true
    item-despawn-rate: 6000
    arrow-despawn-rate: 1200
    trident-despawn-rate: 1200
    wither-spawn-sound-radius: 0
    hanging-tick-frequency: 100
    zombie-aggressive-towards-villager: true
    nerf-spawner-mobs: false
    growth:
      cactus-modifier: 10
      cane-modifier: 10
      melon-modifier: 10
      mushroom-modifier: 10
      pumpkin-modifier: 10
      sapling-modifier: 10
      beetroot-modifier: 10
      carrot-modifier: 10
      potato-modifier: 10
      wheat-modifier: 10
      netherwart-modifier: 10
      vine-modifier: 10
      cocoa-modifier: 10
      bamboo-modifier: 10
      sweetberry-modifier: 10
      kelp-modifier: 10
    entity-activation-range:
      water: 8
      animals: 16
      monsters: 16
      raiders: 24
      misc: 8
      tick-inactive-villagers: false
    merge-radius:
      exp: 10.0
      item: 5
    entity-tracking-range:
      players: 24
      animals: 24
      monsters: 24
      misc: 16
      other: 32
    ticks-per:
      hopper-transfer: 16
      hopper-check: 4
    hunger:
      jump-walk-exhaustion: 0.05
      jump-sprint-exhaustion: 0.2
      combat-exhaustion: 0.1
      regen-exhaustion: 6.0
      swim-multiplier: 0.01
      sprint-multiplier: 0.1
      other-multiplier: 0.0
    max-tick-time:
      tile: 200
      entity: 200
    squid-spawn-range:
      min: 45.0"""
    var paper = """# This is the main configuration file for Paper.
# As you can see, there's tons to configure. Some options may impact gameplay, so use
# with caution, and make sure you know what each option does before configuring.
# 
# If you need help with the configuration or have any questions related to Paper,
# join us in our Discord or IRC channel.
# 
# Discord: https://paperdiscord.emc.gs
# IRC: #paper @ irc.spi.gt ( https://irc.spi.gt/iris/?channels=paper )
# Website: https://papermc.io/ 
# Docs: https://paper.readthedocs.org/ 

verbose: false
config-version: 20
settings:
  load-permissions-yml-before-plugins: true
  bungee-online-mode: true
  region-file-cache-size: 256
  incoming-packet-spam-threshold: 300
  use-alternative-luck-formula: false
  save-player-data: true
  suggest-player-names-when-null-tab-completions: true
  enable-player-collisions: true
  save-empty-scoreboard-teams: false
  velocity-support:
    enabled: false
    online-mode: false
    secret: ''
  async-chunks:
    enable: true
    load-threads: -1
  watchdog:
    early-warning-every: 5000
    early-warning-delay: 10000
  spam-limiter:
    tab-spam-increment: 1
    tab-spam-limit: 500
  book-size:
    page-max: 2560
    total-multiplier: 0.98
messages:
  no-permission: '&cI''m sorry, but you do not have permission to perform this command.
    Please contact the server administrators if you believe that this is in error.'
  kick:
    authentication-servers-down: ''
    connection-throttle: Connection throttled! Please wait before reconnecting.
    flying-player: Flying is not enabled on this server
    flying-vehicle: Flying is not enabled on this server
timings:
  enabled: true
  verbose: true
  server-name-privacy: false
  hidden-config-entries:
  - database
  - settings.bungeecord-addresses
  history-interval: 300
  history-length: 3600
  server-name: Mapmaking Megaserver
world-settings:
  default:
    fixed-chunk-inhabited-time: -1
    optimize-explosions: true
    use-vanilla-world-scoreboard-name-coloring: false
    enable-treasure-maps: true
    treasure-maps-return-already-discovered: false
    prevent-moving-into-unloaded-chunks: false
    disable-teleportation-suffocation-check: false
    experience-merge-max-value: -1
    max-auto-save-chunks-per-tick: 24
    per-player-mob-spawns: false
    remove-corrupt-tile-entities: false
    filter-nbt-data-from-spawn-eggs-and-related: true
    max-entity-collisions: 4
    disable-creeper-lingering-effect: false
    duplicate-uuid-resolver: saferegen
    duplicate-uuid-saferegen-delete-range: 32
    falling-block-height-nerf: 0
    tnt-entity-height-nerf: 0
    disable-thunder: false
    skeleton-horse-thunder-spawn-chance: 0.01
    disable-ice-and-snow: false
    keep-spawn-loaded-range: 10
    keep-spawn-loaded: true
    auto-save-interval: -1
    armor-stands-do-collision-entity-lookups: true
    count-all-mobs-for-spawning: false
    seed-based-feature-search: true
    nether-ceiling-void-damage-height: 0
    water-over-lava-flow-speed: 5
    grass-spread-tick-rate: 1
    bed-search-radius: 1
    use-faster-eigencraft-redstone: true
    fix-zero-tick-instant-grow-farms: true
    allow-non-player-entities-on-scoreboards: false
    portal-search-radius: 128
    portal-create-radius: 16
    disable-explosion-knockback: false
    container-update-tick-rate: 1
    parrots-are-unaffected-by-player-movement: false
    non-player-arrow-despawn-rate: -1
    creative-arrow-despawn-rate: -1
    prevent-tnt-from-moving-in-water: false
    iron-golems-can-spawn-in-air: false
    armor-stands-tick: true
    entities-target-with-follow-range: false
    spawner-nerfed-mobs-should-jump: false
    baby-zombie-movement-modifier: 0.5
    zombie-villager-infection-chance: -1.0
    allow-leashing-undead-horse: false
    all-chunks-are-slime-chunks: false
    mob-spawner-tick-rate: 1
    game-mechanics:
      scan-for-legacy-ender-dragon: true
      disable-pillager-patrols: false
      disable-unloaded-chunk-enderpearl-exploit: true
      nerf-pigmen-from-nether-portals: false
      disable-chest-cat-detection: false
      shield-blocking-delay: 5
      disable-end-credits: false
      disable-player-crits: false
      disable-sprint-interruption-on-attack: false
      disable-relative-projectile-velocity: false
    lightning-strike-distance-limit:
      sound: -1
      impact-sound: -1
      flash: -1
    frosted-ice:
      enabled: true
      delay:
        min: 20
        max: 40
    lootables:
      auto-replenish: false
      restrict-player-reloot: true
      reset-seed-on-fill: true
      max-refills: -1
      refresh-min: 12h
      refresh-max: 2d
    max-growth-height:
      cactus: 3
      reeds: 3
    alt-item-despawn-rate:
      enabled: false
      items:
        COBBLESTONE: 300
    hopper:
      cooldown-when-full: true
      disable-move-event: false
    fishing-time-range:
      MinimumTicks: 100
      MaximumTicks: 600
    despawn-ranges:
      soft: 32
      hard: 128
    squid-spawn-height:
      maximum: 0.0
    anti-xray:
      enabled: false
      engine-mode: 1
      chunk-edge-mode: 2
      max-chunk-section-index: 3
      update-radius: 2
      hidden-blocks:
      - gold_ore
      - iron_ore
      - coal_ore
      - lapis_ore
      - mossy_cobblestone
      - obsidian
      - chest
      - diamond_ore
      - redstone_ore
      - clay
      - emerald_ore
      - ender_chest
      replacement-blocks:
      - stone
      - oak_planks
    generator-settings:
      flat-bedrock: false"""
}