package net.fabricmc.example.mixin;

public class tick {
    public void tick(BooleanSupplier shouldKeepTicking) {
        Profiler profiler = this.getProfiler();
        this.inBlockTick = true;
        profiler.push("world border");
        this.getWorldBorder().tick();
        profiler.swap("weather");
        boolean bl = this.isRaining();
        if (this.getDimension().hasSkyLight()) {
           if (this.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE)) {
              int i = this.field_24456.getClearWeatherTime();
              int j = this.field_24456.getThunderTime();
              int k = this.field_24456.getRainTime();
              boolean bl2 = this.properties.isThundering();
              boolean bl3 = this.properties.isRaining();
              if (i > 0) {
                 --i;
                 j = bl2 ? 0 : 1;
                 k = bl3 ? 0 : 1;
                 bl2 = false;
                 bl3 = false;
              } else {
                 if (j > 0) {
                    --j;
                    if (j == 0) {
                       bl2 = !bl2;
                    }
                 } else if (bl2) {
                    j = this.random.nextInt(12000) + 3600;
                 } else {
                    j = this.random.nextInt(168000) + 12000;
                 }
  
                 if (k > 0) {
                    --k;
                    if (k == 0) {
                       bl3 = !bl3;
                    }
                 } else if (bl3) {
                    k = this.random.nextInt(12000) + 12000;
                 } else {
                    k = this.random.nextInt(168000) + 12000;
                 }
              }
  
              this.field_24456.setThunderTime(j);
              this.field_24456.setRainTime(k);
              this.field_24456.setClearWeatherTime(i);
              this.field_24456.setThundering(bl2);
              this.field_24456.setRaining(bl3);
           }
  
           this.thunderGradientPrev = this.thunderGradient;
           if (this.properties.isThundering()) {
              this.thunderGradient = (float)((double)this.thunderGradient + 0.01D);
           } else {
              this.thunderGradient = (float)((double)this.thunderGradient - 0.01D);
           }
  
           this.thunderGradient = MathHelper.clamp(this.thunderGradient, 0.0F, 1.0F);
           this.rainGradientPrev = this.rainGradient;
           if (this.properties.isRaining()) {
              this.rainGradient = (float)((double)this.rainGradient + 0.01D);
           } else {
              this.rainGradient = (float)((double)this.rainGradient - 0.01D);
           }
  
           this.rainGradient = MathHelper.clamp(this.rainGradient, 0.0F, 1.0F);
        }
  
        if (this.rainGradientPrev != this.rainGradient) {
           this.server.getPlayerManager().sendToDimension(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.RAIN_GRADIENT_CHANGED, this.rainGradient), this.getRegistryKey());
        }
  
        if (this.thunderGradientPrev != this.thunderGradient) {
           this.server.getPlayerManager().sendToDimension(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.THUNDER_GRADIENT_CHANGED, this.thunderGradient), this.getRegistryKey());
        }
  
        if (bl != this.isRaining()) {
           if (bl) {
              this.server.getPlayerManager().sendToAll(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.RAIN_STOPPED, 0.0F));
           } else {
              this.server.getPlayerManager().sendToAll(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.RAIN_STARTED, 0.0F));
           }
  
           this.server.getPlayerManager().sendToAll(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.RAIN_GRADIENT_CHANGED, this.rainGradient));
           this.server.getPlayerManager().sendToAll(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.THUNDER_GRADIENT_CHANGED, this.thunderGradient));
        }
  
        if (this.allPlayersSleeping && this.players.stream().noneMatch((serverPlayerEntity) -> {
           return !serverPlayerEntity.isSpectator() && !serverPlayerEntity.isSleepingLongEnough();
        })) {
           this.allPlayersSleeping = false;
           if (this.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
              long l = this.properties.getTimeOfDay() + 24000L;
              this.method_29199(l - l % 24000L);
           }
  
           this.wakeSleepingPlayers();
           if (this.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE)) {
              this.resetWeather();
           }
        }
  
        this.calculateAmbientDarkness();
        this.tickTime();
        profiler.swap("chunkSource");
        this.getChunkManager().tick(shouldKeepTicking);
        profiler.swap("tickPending");
        if (!this.isDebugWorld()) {
           this.blockTickScheduler.tick();
           this.fluidTickScheduler.tick();
        }
  
        profiler.swap("raid");
        this.raidManager.tick();
        profiler.swap("blockEvents");
        this.processSyncedBlockEvents();
        this.inBlockTick = false;
        profiler.swap("entities");
        boolean bl4 = !this.players.isEmpty() || !this.getForcedChunks().isEmpty();
        if (bl4) {
           this.resetIdleTimeout();
        }
  
        if (bl4 || this.idleTimeout++ < 300) {
           if (this.enderDragonFight != null) {
              this.enderDragonFight.tick();
           }
  
           this.inEntityTick = true;
           ObjectIterator objectIterator = this.entitiesById.int2ObjectEntrySet().iterator();
  
           label164:
           while(true) {
              Entity entity;
              while(true) {
                 if (!objectIterator.hasNext()) {
                    this.inEntityTick = false;
  
                    Entity entity3;
                    while((entity3 = (Entity)this.entitiesToLoad.poll()) != null) {
                       this.loadEntityUnchecked(entity3);
                    }
  
                    this.tickBlockEntities();
                    break label164;
                 }
  
                 Entry<Entity> entry = (Entry)objectIterator.next();
                 entity = (Entity)entry.getValue();
                 Entity entity2 = entity.getVehicle();
                 if (!this.server.shouldSpawnAnimals() && (entity instanceof AnimalEntity || entity instanceof WaterCreatureEntity)) {
                    entity.remove();
                 }
  
                 if (!this.server.shouldSpawnNpcs() && entity instanceof Npc) {
                    entity.remove();
                 }
  
                 profiler.push("checkDespawn");
                 if (!entity.removed) {
                    entity.checkDespawn();
                 }
  
                 profiler.pop();
                 if (entity2 == null) {
                    break;
                 }
  
                 if (entity2.removed || !entity2.hasPassenger(entity)) {
                    entity.stopRiding();
                    break;
                 }
              }
  
              profiler.push("tick");
              if (!entity.removed && !(entity instanceof EnderDragonPart)) {
                 this.tickEntity(this::tickEntity, entity);
              }
  
              profiler.pop();
              profiler.push("remove");
              if (entity.removed) {
                 this.removeEntityFromChunk(entity);
                 objectIterator.remove();
                 this.unloadEntity(entity);
              }
  
              profiler.pop();
           }
        }
  
        profiler.pop();
     }
  
     protected void tickTime() {
        if (this.field_25143) {
           long l = this.properties.getTime() + 1L;
           this.field_24456.method_29034(l);
           this.field_24456.getScheduledEvents().processEvents(this.server, l);
           if (this.properties.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
              this.method_29199(this.properties.getTimeOfDay() + 1L);
           }
  
        }
     }
    
}